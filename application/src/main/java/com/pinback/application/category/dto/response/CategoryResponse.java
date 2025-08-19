package com.pinback.application.category.dto.response;

import com.pinback.domain.category.entity.Category;

public record CategoryResponse(
	Long id,
	String name
) {
	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getName());
	}
}