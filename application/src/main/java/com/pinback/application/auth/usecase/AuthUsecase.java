package com.pinback.application.auth.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.auth.dto.SignUpCommand;
import com.pinback.application.auth.dto.SignUpResponse;
import com.pinback.application.auth.dto.TokenResponse;
import com.pinback.application.auth.service.JwtProvider;
import com.pinback.application.google.dto.response.GoogleLoginResponse;
import com.pinback.application.notification.port.in.SavePushSubscriptionPort;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.application.user.port.out.UserSaveServicePort;
import com.pinback.application.user.port.out.UserUpdateServicePort;
import com.pinback.application.user.port.out.UserValidateServicePort;
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

	@Transactional(readOnly = true)
	public Mono<GoogleLoginResponse> getInfoAndToken(String email) {
		return userGetServicePort.findUserByEmail(email)
			.flatMap(existingUser -> {
				if (existingUser.getRemindDefault() != null) {
					log.info("기존 사용자 로그인 성공: User ID {}", existingUser.getId());

					//Access Token 발급
					String accessToken = jwtProvider.createAccessToken(existingUser.getId());

					return Mono.just(GoogleLoginResponse.loggedIn(
						existingUser.getId(), existingUser.getEmail(), accessToken
					));
				} else {
					log.info("기존 사용자 - 온보딩 미완료 유저 처리: User ID {}", existingUser.getId());

					return Mono.just(GoogleLoginResponse.tempLogin(
						existingUser.getId(), existingUser.getEmail()
					));
				}
			})
			.switchIfEmpty(Mono.defer(() -> {
				log.info("신규 유저 - 임시 유저 생성");
				User tempUser = User.createTempUser(email);

				return userSaveServicePort.saveUser(tempUser)
					.flatMap(savedUser -> {
						return Mono.just(GoogleLoginResponse.tempLogin(savedUser.getId(), savedUser.getEmail()));
					});
			}));
	}

	@Transactional
	public SignUpResponse signUpV2(SignUpCommand signUpCommand) {
		User user = userGetServicePort.findByEmail(signUpCommand.email());
		String accessToken = jwtProvider.createAccessToken(user.getId());
		userUpdateServicePort.updateRemindDefault(user.getId(), signUpCommand.remindDefault());

		savePushSubscriptionPort.savePushSubscription(user, signUpCommand.fcmToken());

		return SignUpResponse.from(accessToken);
	}
}
