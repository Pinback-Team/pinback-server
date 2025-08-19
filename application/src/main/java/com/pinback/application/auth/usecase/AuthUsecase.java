package com.pinback.application.auth.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.service.PushSubscriptionSaveService;
import com.pinback.application.user.service.UserGetService;
import com.pinback.application.user.service.UserSaveService;
import com.pinback.application.user.service.UserValidateService;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;
import com.pinback.application.auth.service.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUsecase {

	private final UserValidateService userValidateService;
	private final UserSaveService userSaveService;
	private final JwtProvider jwtProvider;
	private final PushSubscriptionSaveService pushSubscriptionSaveService;
	private final UserGetService userGetService;

	@Transactional
	public SignUpResponse signUp(SignUpCommand signUpCommand) {
		userValidateService.validateDuplicate(signUpCommand.email());
		User user = userSaveService.save(User.create(signUpCommand.email(), signUpCommand.remindDefault()));
		String accessToken = jwtProvider.createAccessToken(user.getId());

		PushSubscription pushSubscription = PushSubscription.create(
			user,
			signUpCommand.token()
		);

		pushSubscriptionSaveService.save(pushSubscription);

		return SignUpResponse.from(accessToken);
	}

	@Transactional(readOnly = true)
	public TokenResponse getToken(String email) {
		//TODO: GetService 마이그레이션 이후 수정
		User user = userGetService.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));

		String accessToken = jwtProvider.createAccessToken(user.getId());

		return new TokenResponse(accessToken);
	}

	public record SignUpCommand(String email, java.time.LocalTime remindDefault, String token) {
	}

	public static class SignUpResponse {
		public static SignUpResponse from(String accessToken) {
			// Implementation
			return null;
		}
	}

	public static class TokenResponse {
		public TokenResponse(String accessToken) {
			// Implementation
		}
	}
}
