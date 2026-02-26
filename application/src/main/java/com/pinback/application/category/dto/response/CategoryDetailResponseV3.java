package com.pinback.application.category.dto.response;

import com.pinback.domain.category.entity.Category;

public record CategoryDetailResponseV3(
	long categoryId,
	String categoryName,
	boolean isPublic
) {
	public static CategoryDetailResponseV3 from(Category category) {
		return new CategoryDetailResponseV3(category.getId(), category.getName(), category.getIsPublic());
	}
}
