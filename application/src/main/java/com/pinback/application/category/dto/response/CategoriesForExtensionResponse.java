package com.pinback.application.category.dto.response;

import java.util.List;

public record CategoriesForExtensionResponse(
	String recentSaved,
	List<CategoryResponse> categories
) {
	public static CategoriesForExtensionResponse of(String recentSaved, List<CategoryResponse> categories) {
		return new CategoriesForExtensionResponse(recentSaved, categories);
	}
}
