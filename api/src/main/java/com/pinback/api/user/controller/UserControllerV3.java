package com.pinback.api.user.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.user.dto.request.UpdateUserJobRequest;
import com.pinback.application.user.dto.response.UserJobInfoResponse;
import com.pinback.application.user.port.in.UserManagementPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v3/users")
@RequiredArgsConstructor
@Tag(name = "User V3", description = "사용자 관리 API V3")
public class UserControllerV3 {
	private final UserManagementPort userManagementPort;

	@PatchMapping("/job")
	public ResponseDto<UserJobInfoResponse> updateUserJob(
		@Parameter(hidden = true) @CurrentUser User user,
		@Valid @RequestBody UpdateUserJobRequest request
	) {
		UserJobInfoResponse response = userManagementPort.updateUserJobInfo(user, request.toCommand());
		return ResponseDto.ok(response);
	}
}
