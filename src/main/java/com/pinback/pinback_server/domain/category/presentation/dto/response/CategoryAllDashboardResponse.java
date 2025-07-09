package com.pinback.pinback_server.domain.category.presentation.dto.response;

import java.util.List;

public record CategoryAllDashboardResponse(
	List<CategoryDashboardResponse> categories
) {
	public static CategoryAllDashboardResponse of(List<CategoryDashboardResponse> categories) {
		return new CategoryAllDashboardResponse(categories);
	}
}
