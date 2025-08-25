package com.pinback.application.auth.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.auth.dto.SignUpCommand;
import com.pinback.application.auth.dto.SignUpResponse;
import com.pinback.application.auth.dto.TokenResponse;
import com.pinback.application.auth.service.JwtProvider;
import com.pinback.application.notification.port.in.SavePushSubscriptionPort;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.application.user.port.out.UserSaveServicePort;
import com.pinback.application.user.port.out.UserValidateServicePort;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUsecase {

	private final UserValidateServicePort userValidateServicePort;
	private final UserSaveServicePort userSaveServicePort;
	private final JwtProvider jwtProvider;

	private final SavePushSubscriptionPort savePushSubscriptionPort;
	private final UserGetServicePort userGetServicePort;

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
}
