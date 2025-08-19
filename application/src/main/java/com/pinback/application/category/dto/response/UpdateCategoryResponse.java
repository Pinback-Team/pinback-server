package com.pinback.application.category.dto.response;

import com.pinback.domain.category.entity.Category;

public record UpdateCategoryResponse(
	Long id,
	String name
) {
	public static UpdateCategoryResponse from(Category category) {
		return new UpdateCategoryResponse(category.getId(), category.getName());
	}
}