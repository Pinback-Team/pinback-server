package com.pinback.application.test.dto.response;

import java.util.List;

public record CategoriesTestResponse(
	List<String> categories
) {
	public static CategoriesTestResponse of(List<String> categories) {
		return new CategoriesTestResponse(categories);
	}
}
