package com.pinback.pinback_server.domain.category.domain.repository;

import java.util.List;
import java.util.UUID;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForDashboard;

public interface CategoryRepositoryCustom {
	List<Category> findAllForExtension(UUID userId);

	CategoriesForDashboard findAllForDashboard(UUID userId);
}
