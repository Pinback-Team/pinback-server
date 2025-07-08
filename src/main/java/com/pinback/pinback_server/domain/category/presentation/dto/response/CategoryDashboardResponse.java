package com.pinback.pinback_server.domain.category.presentation.dto.response;

public record CategoryDashboardResponse(
	long categoryId,
	String categoryName,
	int unreadCount
) {
}
