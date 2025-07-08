package com.pinback.pinback_server.domain.category.presentation.dto.response;

public record CreateCategoryResponse(
	long id,
	String categoryName
) {
	public static CreateCategoryResponse of(long id, String categoryName) {
		return new CreateCategoryResponse(id, categoryName);
	}
}
