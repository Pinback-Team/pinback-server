package com.pinback.pinback_server.domain.category.presentation.dto.response;

import com.pinback.pinback_server.domain.category.domain.entity.Category;

public record UpdateCategoryResponse(
	long id,
	String categoryName
) {
	public static UpdateCategoryResponse from(Category category) {
		return new UpdateCategoryResponse(category.getId(), category.getName());
	}
}
