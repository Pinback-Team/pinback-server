package com.pinback.api.user.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.user.dto.response.UserInfoResponse;
import com.pinback.application.user.dto.response.UserRemindInfoResponse;
import com.pinback.application.user.port.in.UserManagementPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

	private final UserManagementPort userManagementPort;

	@Operation(summary = "사용자 정보 조회", description = "현재 사용자의 정보를 조회합니다")
	@GetMapping("/acorns")
	public ResponseDto<UserInfoResponse> getUserAcorns(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		UserInfoResponse response = userManagementPort.getUserInfo(user, LocalDateTime.now());
		return ResponseDto.ok(response);
	}

	@Operation(summary = "사용자 리마인더 정보 조회", description = "사용자의 리마인더 설정 정보를 조회합니다")
	@GetMapping("/remind-time")
	public ResponseDto<UserRemindInfoResponse> getRemindInfo(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		UserRemindInfoResponse response = userManagementPort.getUserRemindInfo(user, LocalDateTime.now());
		return ResponseDto.ok(response);
	}
}
