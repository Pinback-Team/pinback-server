package com.pinback.infrastructure.category.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.dto.CategoriesForDashboard;

public interface CategoryRepositoryCustom {
	List<Category> findAllForExtension(UUID userId);

	CategoriesForDashboard findAllForDashboard(UUID userId);
	
	Set<CategoryColor> findColorsByUser(User user);
}
