package com.pinback.pinback_server.domain.auth.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.auth.application.AuthUsecase;
import com.pinback.pinback_server.domain.auth.presentation.dto.request.SignUpRequest;
import com.pinback.pinback_server.domain.auth.presentation.dto.response.SignUpResponse;
import com.pinback.pinback_server.domain.auth.presentation.dto.response.TokenResponse;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthUsecase authUsecase;

	@PostMapping("/signup")
	public ResponseDto<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		SignUpResponse response = authUsecase.signUp(signUpRequest.toCommand());
		return ResponseDto.created(response);
	}

	@GetMapping("/token")
	public ResponseDto<TokenResponse> getToken(@Valid @RequestParam String email) {
		TokenResponse response = authUsecase.getToken(email);

		return ResponseDto.ok(response);
	}
}
