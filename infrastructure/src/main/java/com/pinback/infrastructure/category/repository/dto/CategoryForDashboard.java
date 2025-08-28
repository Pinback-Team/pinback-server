package com.pinback.infrastructure.category.repository.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CategoryForDashboard {
	private final Long id;
	private final String name;
	private final Long unreadCount;

	@QueryProjection
	public CategoryForDashboard(Long id, String name, Long unreadCount) {
		this.id = id;
		this.name = name;
		this.unreadCount = unreadCount;
	}
}
