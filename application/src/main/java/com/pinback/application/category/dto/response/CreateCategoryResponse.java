package com.pinback.application.category.dto.response;

public record CreateCategoryResponse(
	Long categoryId,
	String categoryName
) {
	public static CreateCategoryResponse of(Long categoryId, String categoryName) {
		return new CreateCategoryResponse(categoryId, categoryName);
	}
}
