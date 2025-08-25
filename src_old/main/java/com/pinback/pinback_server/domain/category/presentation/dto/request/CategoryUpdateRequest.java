package com.pinback.pinback_server.domain.category.presentation.dto.request;

import com.pinback.pinback_server.domain.category.application.command.CategoryUpdateCommand;
import com.pinback.pinback_server.domain.category.exception.CategoryInvalidException;
import com.pinback.pinback_server.global.common.util.TextUtil;
import com.pinback.pinback_server.global.exception.TextLengthOverException;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
	@NotBlank(message = "카테고리 이름은 비어있을 수 없습니다.")
	String categoryName
) {
	private static final int MAX_CATEGORY_NAME_LENGTH = 10;

	public CategoryUpdateRequest {
		if (categoryName.contains(" ")) {
			throw new CategoryInvalidException();
		}

		int nameLength = TextUtil.countGraphemeClusters(categoryName);

		if (nameLength > MAX_CATEGORY_NAME_LENGTH) {
			throw new TextLengthOverException();
		}
	}

	public CategoryUpdateCommand toCommand() {
		return new CategoryUpdateCommand(categoryName);
	}
}
