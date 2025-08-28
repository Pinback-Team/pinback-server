package com.pinback.application.category.port.in;

import com.pinback.application.category.dto.response.CategoriesForDashboardResponse;
import com.pinback.application.category.dto.response.CategoriesForExtensionResponse;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface GetCategoryPort {
	CategoriesForExtensionResponse getAllCategoriesForExtension(User user);

	CategoriesForDashboardResponse getAllCategoriesForDashboard(User user);
	
	Category getCategoryAndUser(long categoryId, User user);
}
