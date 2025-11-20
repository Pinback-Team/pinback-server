package com.pinback.application.google.usecase;

import org.springframework.stereotype.Service;

import com.pinback.application.google.dto.GoogleLoginCommand;
import com.pinback.application.google.dto.response.GoogleUserInfoResponse;
import com.pinback.application.google.port.out.GoogleOAuthPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GoogleUsecase {
	private final GoogleOAuthPort googleOAuthPort;

	public Mono<GoogleUserInfoResponse> getUserInfo(GoogleLoginCommand command) {

		String code = command.code();

		return googleOAuthPort.fetchUserInfo(code);
	}
}
