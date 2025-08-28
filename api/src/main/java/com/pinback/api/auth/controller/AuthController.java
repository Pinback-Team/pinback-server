package com.pinback.api.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.auth.dto.request.SignUpRequest;
import com.pinback.application.auth.dto.SignUpResponse;
import com.pinback.application.auth.dto.TokenResponse;
import com.pinback.application.auth.usecase.AuthUsecase;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 관리 API")
public class AuthController {

	private final AuthUsecase authUsecase;

	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
	@PostMapping("/signup")
	public ResponseDto<SignUpResponse> signUp(
		@Valid @RequestBody SignUpRequest request
	) {
		SignUpResponse response = authUsecase.signUp(request.toCommand());
		return ResponseDto.created(response);
	}

	@Operation(summary = "로그인", description = "이메일로 로그인합니다")
	@PostMapping("/signin")
	public ResponseDto<TokenResponse> signIn(
		@Valid @RequestBody SignUpRequest request
	) {
		TokenResponse response = authUsecase.getToken(request.email());
		return ResponseDto.ok(response);
	}
}
