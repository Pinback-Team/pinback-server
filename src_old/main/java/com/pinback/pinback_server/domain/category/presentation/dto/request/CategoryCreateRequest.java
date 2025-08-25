package com.pinback.pinback_server.domain.category.presentation.dto.request;

import com.pinback.pinback_server.domain.category.application.command.CategoryCreateCommand;
import com.pinback.pinback_server.domain.category.exception.CategoryInvalidException;
import com.pinback.pinback_server.global.common.util.TextUtil;
import com.pinback.pinback_server.global.exception.TextLengthOverException;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
	@NotBlank(message = "카테고리 이름은 비어있을 수 없습니다.")
	String categoryName
) {
	private static final int MAX_CATEGORY_NAME_LENGTH = 10;

	public CategoryCreateRequest {
		if (categoryName.contains(" ")) {
			throw new CategoryInvalidException();
		}
		int nameLength = TextUtil.countGraphemeClusters(categoryName);

		if (nameLength > MAX_CATEGORY_NAME_LENGTH) {
			throw new TextLengthOverException();
		}
	}

	public CategoryCreateCommand toCommand() {
		return new CategoryCreateCommand(categoryName);
	}
}
