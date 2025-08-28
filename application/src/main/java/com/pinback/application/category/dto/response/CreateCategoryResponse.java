package com.pinback.application.category.dto.response;

public record CreateCategoryResponse(
	Long id,
	String name
) {
	public static CreateCategoryResponse of(Long id, String name) {
		return new CreateCategoryResponse(id, name);
	}
}
