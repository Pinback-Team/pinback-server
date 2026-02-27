package com.pinback.application.category.dto.response;

import com.pinback.domain.category.enums.CategoryColor;

public record CreateCategoryResponseV3(
	Long categoryId,
	String categoryName,
	String categoryColor,
	Boolean isPublic
) {
	public static CreateCategoryResponseV3 of(Long categoryId, String categoryName, CategoryColor categoryColor) {
		return new CreateCategoryResponseV3(categoryId, categoryName, categoryColor.toString(), false);
	}

	public static CreateCategoryResponseV3 of(Long categoryId, String categoryName, CategoryColor categoryColor,
		boolean isPublic) {
		return new CreateCategoryResponseV3(categoryId, categoryName, categoryColor.toString(), isPublic);
	}
}
