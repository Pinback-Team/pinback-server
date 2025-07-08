package com.pinback.pinback_server.domain.category.domain.repository.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoriesForDashboard {
	private List<CategoryForDashboard> categories;

	@QueryProjection
	public CategoriesForDashboard(List<CategoryForDashboard> categories) {
		this.categories = categories;
	}
}
