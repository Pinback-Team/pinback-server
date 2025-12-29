package com.pinback.application.google.port.out;

import com.pinback.application.google.dto.response.GoogleUserInfoResponse;

import reactor.core.publisher.Mono;

public interface GoogleOAuthPort {
	Mono<GoogleUserInfoResponse> fetchUserInfo(String code);
}
