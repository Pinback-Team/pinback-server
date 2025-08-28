package com.pinback.api.category.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.category.dto.request.CreateCategoryRequest;
import com.pinback.api.category.dto.request.UpdateCategoryRequest;
import com.pinback.application.category.dto.response.CategoriesForDashboardResponse;
import com.pinback.application.category.dto.response.CategoriesForExtensionResponse;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.application.category.port.in.CreateCategoryPort;
import com.pinback.application.category.port.in.DeleteCategoryPort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.category.port.in.UpdateCategoryPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

	private final CreateCategoryPort createCategoryPort;
	private final GetCategoryPort getCategoryPort;
	private final UpdateCategoryPort updateCategoryPort;
	private final DeleteCategoryPort deleteCategoryPort;

	@Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다")
	@PostMapping
	public ResponseDto<CreateCategoryResponse> createCategory(
		@Parameter(hidden = true) @CurrentUser User user,
		@Valid @RequestBody CreateCategoryRequest request
	) {
		CreateCategoryResponse response = createCategoryPort.createCategory(user, request.toCommand());
		return ResponseDto.ok(response);
	}

	@Operation(summary = "대시보드용 카테고리 조회", description = "대시보드에서 사용할 카테고리 목록을 조회합니다")
	@GetMapping("/dashboard")
	public ResponseDto<CategoriesForDashboardResponse> getCategoriesForDashboard(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		CategoriesForDashboardResponse response = getCategoryPort.getAllCategoriesForDashboard(user);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "확장 프로그램용 카테고리 조회", description = "확장 프로그램에서 사용할 카테고리 목록을 조회합니다")
	@GetMapping("/extension")
	public ResponseDto<CategoriesForExtensionResponse> getCategoriesForExtension(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		CategoriesForExtensionResponse response = getCategoryPort.getAllCategoriesForExtension(user);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "카테고리 수정", description = "카테고리 정보를 수정합니다")
	@PutMapping("/{categoryId}")
	public ResponseDto<UpdateCategoryResponse> updateCategory(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
		@Valid @RequestBody UpdateCategoryRequest request
	) {
		UpdateCategoryResponse response = updateCategoryPort.updateCategory(user, categoryId, request.toCommand());
		return ResponseDto.ok(response);
	}

	@Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다")
	@DeleteMapping("/{categoryId}")
	public ResponseDto<Void> deleteCategory(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "카테고리 ID") @PathVariable Long categoryId
	) {
		deleteCategoryPort.deleteCategory(user, categoryId);
		return ResponseDto.ok();
	}
}
