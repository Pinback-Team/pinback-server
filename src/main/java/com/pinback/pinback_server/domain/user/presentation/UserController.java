package com.pinback.pinback_server.domain.user.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.user.application.UserManagementUsecase;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.presentation.dto.response.UserInfoResponse;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final UserManagementUsecase userManagementUsecase;

	@GetMapping("/acorns")
	public ResponseDto<UserInfoResponse> getUserAcorns(
		@CurrentUser User user
	) {
		UserInfoResponse userInfoResponse = userManagementUsecase.getUserInfo(user);
		return ResponseDto.ok(userInfoResponse);
	}
}