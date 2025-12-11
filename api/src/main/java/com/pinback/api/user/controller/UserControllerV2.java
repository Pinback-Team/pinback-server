package com.pinback.api.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.user.dto.response.UserGoogleProfileResponse;
import com.pinback.application.user.dto.response.UserProfileInfoResponse;
import com.pinback.application.user.port.in.UserManagementPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
@Tag(name = "UserV2", description = "사용자 관리 API V2")
public class UserControllerV2 {
	private final UserManagementPort userManagementPort;

	@GetMapping("/me")
	public ResponseDto<UserProfileInfoResponse> getUserProfileInfo(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		UserProfileInfoResponse response = userManagementPort.getUserProfileInfo(user);
		return ResponseDto.ok(response);
	}

	@GetMapping("/me/google-profile")
	public ResponseDto<UserGoogleProfileResponse> getUserGoogleProfile(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		UserGoogleProfileResponse response = userManagementPort.getUserGoogleProfile(user);
		return ResponseDto.ok(response);
	}
}
