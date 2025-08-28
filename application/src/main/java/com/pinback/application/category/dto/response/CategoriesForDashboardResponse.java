package com.pinback.application.category.dto.response;

import java.util.List;

public record CategoriesForDashboardResponse(
	List<CategoryDashboardResponse> categories
) {
	public static CategoriesForDashboardResponse of(List<CategoryDashboardResponse> categories) {
		return new CategoriesForDashboardResponse(categories);
	}
}
