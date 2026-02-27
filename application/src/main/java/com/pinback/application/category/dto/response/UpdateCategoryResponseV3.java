package com.pinback.application.category.dto.response;

import com.pinback.domain.category.entity.Category;

public record UpdateCategoryResponseV3(
	long categoryId,
	String categoryName,
	Boolean isPublic
) {
	public static UpdateCategoryResponseV3 from(Category category) {
		return new UpdateCategoryResponseV3(category.getId(), category.getName(), category.getIsPublic());
	}
}
