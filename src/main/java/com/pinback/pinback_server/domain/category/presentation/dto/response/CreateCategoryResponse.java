package com.pinback.pinback_server.domain.category.presentation.dto.response;

public record CreateCategoryResponse(
	Long id,
	String categoryName
) {
	public static CreateCategoryResponse of(Long id, String categoryName) {
		return new CreateCategoryResponse(id, categoryName);
	}
}
