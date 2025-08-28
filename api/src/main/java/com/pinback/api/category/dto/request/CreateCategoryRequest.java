package com.pinback.api.category.dto.request;

import com.pinback.application.category.dto.command.CreateCategoryCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 생성 요청")
public record CreateCategoryRequest(
	@Schema(description = "카테고리 이름", example = "개발")
	@NotBlank(message = "카테고리 이름은 필수입니다")
	@Size(max = 50, message = "카테고리 이름은 50자 이하로 입력해주세요")
	String categoryName
) {
	public CreateCategoryCommand toCommand() {
		return new CreateCategoryCommand(categoryName);
	}
}
