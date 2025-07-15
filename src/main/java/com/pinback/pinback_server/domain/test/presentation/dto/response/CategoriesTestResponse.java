package com.pinback.pinback_server.domain.test.presentation.dto.response;

import java.util.List;

public record CategoriesTestResponse(
	List<String> categories
) {
	public static CategoriesTestResponse of(List<String> categories) {
		return new CategoriesTestResponse(categories);
	}
}
