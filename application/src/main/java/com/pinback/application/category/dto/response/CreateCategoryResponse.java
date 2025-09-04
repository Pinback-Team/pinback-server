package com.pinback.application.category.dto.response;

import com.pinback.domain.category.enums.CategoryColor;

public record CreateCategoryResponse(
	Long categoryId,
	String categoryName,
	String categoryColor
) {
	public static CreateCategoryResponse of(Long categoryId, String categoryName, CategoryColor categoryColor) {
		return new CreateCategoryResponse(categoryId, categoryName, categoryColor.toString());
	}
}
