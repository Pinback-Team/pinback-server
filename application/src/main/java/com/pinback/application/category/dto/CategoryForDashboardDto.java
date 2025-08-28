package com.pinback.application.category.dto;

public record CategoryForDashboardDto(
	Long id,
	String name,
	long unreadCount
) {}
