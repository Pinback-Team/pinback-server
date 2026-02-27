package com.pinback.application.category.dto.response;

import com.pinback.domain.category.entity.Category;

public record CategoryResponse(
	long categoryId,
	String categoryName,
	String categoryColor
) {
	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getColor().toString());
	}

	public static CategoryResponse of(long id, String name, String color) {
		return new CategoryResponse(id, name, color);
	}
}
