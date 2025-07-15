package com.pinback.pinback_server.domain.category.presentation.dto.response;

public record CreateCategoryResponse(
	long categoryId,
	String categoryName
) {
	public static CreateCategoryResponse of(long categoryId, String categoryName) {
		return new CreateCategoryResponse(categoryId, categoryName);
	}
}
