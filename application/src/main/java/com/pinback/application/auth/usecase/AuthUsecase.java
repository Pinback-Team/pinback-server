package com.pinback.application.auth.usecase;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.auth.dto.SignUpCommand;
import com.pinback.application.auth.dto.SignUpResponse;
import com.pinback.application.auth.dto.TokenResponse;
import com.pinback.application.auth.service.JwtProvider;
import com.pinback.application.config.ProfileImageConfig;
import com.pinback.application.google.dto.response.GoogleLoginResponse;
import com.pinback.application.notification.port.in.SavePushSubscriptionPort;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.application.user.port.out.UserSaveServicePort;
import com.pinback.application.user.port.out.UserUpdateServicePort;
import com.pinback.application.user.port.out.UserValidateServicePort;
import com.pinback.application.user.usecase.UserOAuthUsecase;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUsecase {

	private final UserValidateServicePort userValidateServicePort;
	private final UserSaveServicePort userSaveServicePort;
	private final JwtProvider jwtProvider;

	private final SavePushSubscriptionPort savePushSubscriptionPort;
	private final UserGetServicePort userGetServicePort;
	private final UserUpdateServicePort userUpdateServicePort;
	private final UserOAuthUsecase userOAuthUsecase;
	private final ProfileImageConfig profileImageConfig;

	@Transactional
	public SignUpResponse signUp(SignUpCommand signUpCommand) {
		userValidateServicePort.validateDuplicateEmail(signUpCommand.email());

		User user = userSaveServicePort.save(User.create(signUpCommand.email(), signUpCommand.remindDefault()));
		String accessToken = jwtProvider.createAccessToken(user.getId());

		savePushSubscriptionPort.savePushSubscription(user, signUpCommand.fcmToken());

		return SignUpResponse.from(accessToken);
	}

	@Transactional(readOnly = true)
	public TokenResponse getToken(String email) {
		User user = userGetServicePort.findByEmail(email);

		String accessToken = jwtProvider.createAccessToken(user.getId());

		return new TokenResponse(accessToken);
	}

	@Transactional
	public Mono<GoogleLoginResponse> getInfoAndToken(String email, String pictureUrl, String name) {
		return userGetServicePort.findUserByEmail(email)
			.flatMap(existingUser -> {

				Mono<User> updateMono = applyMissingUserInfo(existingUser, pictureUrl, name);
				return updateMono
					.flatMap(updatedUser -> {
						if (updatedUser.getRemindDefault() != null && updatedUser.getProfileImage() != null) {
							log.info("기존 사용자 로그인 성공: User ID {}", updatedUser.getId());

							//Access Token 발급
							String accessToken = jwtProvider.createAccessToken(updatedUser.getId());

							return Mono.just(GoogleLoginResponse.loggedIn(
								updatedUser.getId(), updatedUser.getEmail(), accessToken
							));
						} else {
							log.info("기존 사용자 - 온보딩 미완료 유저 처리: User ID {}", updatedUser.getId());

							return Mono.just(GoogleLoginResponse.tempLogin(
								updatedUser.getId(), updatedUser.getEmail()
							));
						}
					});
			})
			.switchIfEmpty(Mono.defer(() -> {
				log.info("신규 유저 - 임시 유저 생성");
				User tempUser = User.createTempUser(email, name);

				return userSaveServicePort.saveUser(tempUser)
					.flatMap(savedUser -> {
						// 1. S3 이미지 저장 서비스 호출
						Mono<String> s3UrlMono = userOAuthUsecase.saveProfileImage(savedUser.getId(), pictureUrl);
						return s3UrlMono.flatMap(s3Url -> {
							// 2. S3 URL로 유저 엔티티 업데이트
							savedUser.updateGoogleProfileImage(s3Url);
							return userUpdateServicePort.updateUser(savedUser);

						}).then(
							// 3. 최종 응답 반환 (이미지 저장 트랜잭션 완료 후)
							Mono.just(GoogleLoginResponse.tempLogin(savedUser.getId(), savedUser.getEmail()))
						);
					});
			}));
	}

	@Transactional
	public SignUpResponse signUpV2(SignUpCommand signUpCommand) {
		User user = userGetServicePort.findByEmail(signUpCommand.email());
		String accessToken = jwtProvider.createAccessToken(user.getId());
		userUpdateServicePort.updateRemindDefault(user.getId(), signUpCommand.remindDefault());

		savePushSubscriptionPort.savePushSubscription(user, signUpCommand.fcmToken());
		String profileImage = matchingProfileImage(signUpCommand.remindDefault());
		userUpdateServicePort.updateProfileImage(user.getId(), profileImage);

		return SignUpResponse.from(accessToken);
	}

	private Mono<User> applyMissingUserInfo(User existingUser, String pictureUrl, String name) {
		// 1. 이름 업데이트
		boolean nameUpdated = false;
		if (existingUser.getUsername() == null && name != null) {
			existingUser.updateName(name);
			nameUpdated = true;
		}

		// 2. 구글 프로필 업데이트
		Mono<Void> imageUpdateMono = Mono.empty();
		boolean imageUpdateNeeded = false;
		if (existingUser.getGoogleProfileImage() == null && pictureUrl != null) {
			imageUpdateNeeded = true;
			imageUpdateMono = userOAuthUsecase.saveProfileImage(existingUser.getId(), pictureUrl)
				.doOnNext(existingUser::updateGoogleProfileImage)
				.then();
		}

		// 3. 최종 업데이트
		if (imageUpdateNeeded || nameUpdated) {
			return imageUpdateMono
				.then(userUpdateServicePort.updateUser(existingUser));
		}

		return Mono.just(existingUser);
	}

	private String matchingProfileImage(LocalTime remindDefault) {
		final LocalTime MORNING_TIME = LocalTime.of(9, 0);
		final LocalTime EVENING_TIME = LocalTime.of(20, 0);

		if (remindDefault.equals(MORNING_TIME)) {
			return "IMAGE1";
		} else if (remindDefault.equals(EVENING_TIME)) {
			return "IMAGE2";
		} else {
			return "IMAGE3";
		}
	}
}
