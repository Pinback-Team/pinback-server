package com.pinback.api.test.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.test.dto.request.PushTestRequest;
import com.pinback.application.test.dto.response.CategoriesTestResponse;
import com.pinback.application.test.port.in.TestPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
	private final TestPort testPort;

	@PostMapping("/push")
	public ResponseDto<Void> pushTest(@RequestBody PushTestRequest pushTestRequest) {
		testPort.pushTest(pushTestRequest);
		return ResponseDto.ok();
	}

	@PostMapping("/articles")
	public ResponseDto<Void> createArticles(
		@Parameter(hidden = true) @CurrentUser User user,
		@RequestParam Long categoryId
	) {
		testPort.createArticlesByCategory(user, categoryId);
		return ResponseDto.ok();
	}

	@PostMapping("/categories")
	public ResponseDto<CategoriesTestResponse> categoriesTest(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		CategoriesTestResponse response = testPort.createCategories(user);
		return ResponseDto.ok(response);
	}

	@DeleteMapping("/articles/{categoryId}")
	public ResponseDto<Void> deleteTest(
		@Parameter(hidden = true) @CurrentUser User user,
		@PathVariable Long categoryId
	) {
		testPort.deleteArticlesByCategory(user, categoryId);
		return ResponseDto.ok();
	}

	@GetMapping("/health")
	public ResponseDto<String> healthCheck() {
		return ResponseDto.ok("OK");
	}
}
