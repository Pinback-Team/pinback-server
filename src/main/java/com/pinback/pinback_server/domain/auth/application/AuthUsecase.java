package com.pinback.pinback_server.domain.auth.application;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.auth.application.command.SignUpCommand;
import com.pinback.pinback_server.domain.auth.exception.UserDuplicateException;
import com.pinback.pinback_server.domain.auth.presentation.dto.UserSignUpResponse;
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

	public UserSignUpResponse signUp(SignUpCommand signUpCommand) {
		if (userValidateService.checkDuplicate(signUpCommand.email())) {
			throw new UserDuplicateException();
		}
		User user = userSaveService.save(User.create(signUpCommand.email(), signUpCommand.remindDefault()));
		String accessToken = jwtProvider.createAccessToken(user.getId());

		return UserSignUpResponse.from(accessToken);
	}
}
