package com.pinback.pinback_server.domain.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.auth.application.command.SignUpCommand;
import com.pinback.pinback_server.domain.auth.exception.UserDuplicateException;
import com.pinback.pinback_server.domain.auth.presentation.dto.response.SignUpResponse;
import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;
import com.pinback.pinback_server.domain.notification.domain.service.PushSubscriptionSaveService;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserSaveService;
import com.pinback.pinback_server.domain.user.domain.service.UserValidateService;
import com.pinback.pinback_server.global.common.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUsecase {

	private final UserValidateService userValidateService;
	private final UserSaveService userSaveService;
	private final JwtProvider jwtProvider;
	private final PushSubscriptionSaveService pushSubscriptionSaveService;

	@Transactional
	public SignUpResponse signUp(SignUpCommand signUpCommand) {
		if (userValidateService.checkDuplicate(signUpCommand.email())) {
			throw new UserDuplicateException();
		}
		User user = userSaveService.save(User.create(signUpCommand.email(), signUpCommand.remindDefault()));
		String accessToken = jwtProvider.createAccessToken(user.getId());

		PushSubscription pushSubscription = PushSubscription.from(
			user,
			signUpCommand.token()
		);

		pushSubscriptionSaveService.save(pushSubscription);

		return SignUpResponse.from(accessToken);
	}
}
