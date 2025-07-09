package com.pinback.pinback_server.domain.category.domain.repository.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryForDashboard {
	private Long id;
	private String name;
	private Long unreadCount;

	@QueryProjection
	public CategoryForDashboard(Long id, String name, Long unreadCount) {
		this.id = id;
		this.name = name;
		this.unreadCount = unreadCount;
	}
}
