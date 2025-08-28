package com.pinback.application.category.dto;

import java.util.List;

public record CategoriesForDashboardDto(
	List<CategoryForDashboardDto> categories
) {
}
