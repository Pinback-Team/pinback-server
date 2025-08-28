package com.pinback.application.category.port.out;

import java.util.List;
import java.util.UUID;

import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface CategoryGetServicePort {
	Category findById(long categoryId);

	Category getCategoryAndUser(long categoryId, User user);

	long countCategoriesByUser(User user);

	boolean checkExistsByCategoryNameAndUser(String categoryName, User user);

	List<Category> findAllForExtension(UUID userId);

	CategoriesForDashboardDto findAllForDashboard(UUID userId);
}
