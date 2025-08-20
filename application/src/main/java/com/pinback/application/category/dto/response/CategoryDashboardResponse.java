package com.pinback.application.category.dto.response;

public record CategoryDashboardResponse(
	long id,
	String name,
	long unreadCount
) {
}