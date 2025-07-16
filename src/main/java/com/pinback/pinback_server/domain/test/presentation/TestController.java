package com.pinback.pinback_server.domain.test.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.test.application.TestUsecase;
import com.pinback.pinback_server.domain.test.presentation.dto.request.PushTestRequest;
import com.pinback.pinback_server.domain.test.presentation.dto.response.CategoriesTestResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
	private final TestUsecase testUsecase;

	@PostMapping("/push")
	public ResponseDto<Void> pushTest(@RequestBody PushTestRequest pushTestRequest) {
		testUsecase.pushTest(pushTestRequest);

		return ResponseDto.ok();
	}

	@PostMapping("/articles")
	public ResponseDto<Void> createArticles(
		@CurrentUser User user,
		@RequestParam Long categoryId
	) {
		testUsecase.createByCategory(user, categoryId);

		return ResponseDto.ok();
	}

	@PostMapping("/categories")
	public ResponseDto<CategoriesTestResponse> categoriesTest(
		@CurrentUser User user
	) {
		CategoriesTestResponse response = testUsecase.categoriesTest(user);
		return ResponseDto.ok(response);
	}
}
