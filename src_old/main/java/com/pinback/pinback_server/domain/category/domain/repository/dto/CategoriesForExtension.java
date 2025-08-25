package com.pinback.pinback_server.domain.category.domain.repository.dto;

import java.util.List;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoriesForExtension {
	private List<Category> categories;

	@QueryProjection
	public CategoriesForExtension(List<Category> categories) {
		this.categories = categories;
	}
}
