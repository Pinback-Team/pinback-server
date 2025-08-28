package com.pinback.api.category.dto.request;

import com.pinback.application.category.dto.command.UpdateCategoryCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 수정 요청")
public record UpdateCategoryRequest(
	@Schema(description = "카테고리 이름", example = "수정된 개발")
	@NotBlank(message = "카테고리 이름은 필수입니다")
	@Size(max = 50, message = "카테고리 이름은 50자 이하로 입력해주세요")
	String categoryName
) {
	public UpdateCategoryCommand toCommand() {
		return new UpdateCategoryCommand(categoryName);
	}
}
