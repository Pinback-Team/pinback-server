package com.pinback.pinback_server.domain.category.presentation.dto.request;

import com.pinback.pinback_server.domain.category.application.command.CategoryCreateCommand;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
	@NotBlank(message = "카테고리 이름은 비어있을 수 없습니다.")
	String categoryName
) {
	public CategoryCreateCommand toCommand() {
		return new CategoryCreateCommand(categoryName);
	}
}
