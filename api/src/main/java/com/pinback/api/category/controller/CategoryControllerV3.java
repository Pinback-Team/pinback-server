package com.pinback.api.category.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.category.dto.request.CreateCategoryRequestV3;
import com.pinback.api.category.dto.request.UpdateCategoryRequestV3;
import com.pinback.application.category.dto.response.CreateCategoryResponseV3;
import com.pinback.application.category.dto.response.UpdateCategoryResponseV3;
import com.pinback.application.category.port.in.CreateCategoryPort;
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
@RequestMapping("/api/v3/categories")
@RequiredArgsConstructor
@Tag(name = "CategoryV3", description = "카테고리 관리 API V3")
public class CategoryControllerV3 {
	private final CreateCategoryPort createCategoryPort;
	private final UpdateCategoryPort updateCategoryPort;

	@Operation(summary = "카테고리 생성 V3", description = "공개여부 설정을 추가하여 새로운 카테고리를 생성합니다")
	@PostMapping
	public ResponseDto<CreateCategoryResponseV3> createCategoryV3(
		@Parameter(hidden = true) @CurrentUser User user,
		@Valid @RequestBody CreateCategoryRequestV3 request
	) {
		CreateCategoryResponseV3 response = createCategoryPort.createCategoryV3(user, request.toCommand());
		return ResponseDto.ok(response);
	}

	@Operation(summary = "카테고리 수정 V3", description = "공개여부 설정을 추가하여 카테고리 정보를 수정합니다")
	@PatchMapping("/{categoryId}")
	public ResponseDto<UpdateCategoryResponseV3> updateCategoryV3(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
		@Valid @RequestBody UpdateCategoryRequestV3 request
	) {
		UpdateCategoryResponseV3 response = updateCategoryPort.updateCategoryV3(user, categoryId, request.toCommand());
		return ResponseDto.ok(response);
	}
}
