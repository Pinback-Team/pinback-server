package com.pinback.pinback_server.domain.category.presentation.dto.response;

import com.pinback.pinback_server.domain.category.domain.entity.Category;

public record CategoryDetail(
	long categoryId,
	String categoryName
) {
	public static CategoryDetail of(Category category) {
		return new CategoryDetail(
			category.getId(),
			category.getName()
		);
	}
}
