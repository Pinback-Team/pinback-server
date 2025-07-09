package com.pinback.pinback_server.domain.category.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.category.application.CategoryManagementUsecase;
import com.pinback.pinback_server.domain.category.presentation.dto.request.CategoryCreateRequest;
import com.pinback.pinback_server.domain.category.presentation.dto.request.CategoryUpdateRequest;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryAllDashboardResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryAllExtensionResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CreateCategoryResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.UpdateCategoryResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryManagementUsecase categoryManagementUsecase;

	@PostMapping
	public ResponseDto<CreateCategoryResponse> createCategory(@CurrentUser User user,
		@Valid @RequestBody CategoryCreateRequest request) {
		CreateCategoryResponse response = categoryManagementUsecase.createCategory(user, request.toCommand());

		return ResponseDto.created(response);
	}

	@GetMapping("/extension")
	public ResponseDto<?> getAllExtension(@CurrentUser User user) {
		CategoryAllExtensionResponse response = categoryManagementUsecase.getAllCategoriesExtension(user);
		return ResponseDto.ok(response);
	}

	@GetMapping("/dashboard")
	public ResponseDto<CategoryAllDashboardResponse> getAllDashboard(@CurrentUser User user) {
		CategoryAllDashboardResponse response = categoryManagementUsecase.getAllCategoriesDashboard(user);
		return ResponseDto.ok(response);
	}

	@PatchMapping("/{categoryId}")
	public ResponseDto<UpdateCategoryResponse> updateCategory(
		@CurrentUser User user,
		@PathVariable Long categoryId,
		@Valid @RequestBody CategoryUpdateRequest request
	) {
		UpdateCategoryResponse response = categoryManagementUsecase.updateCategory(user, categoryId,
			request.toCommand());
		return ResponseDto.ok(response);
	}
}

