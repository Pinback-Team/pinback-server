package com.pinback.application.category.dto.response;

public record CategoryDashboardResponse(
	Long id,
	String name,
	long unreadCount
) {}