package com.pinback.application.category.port.in;

import com.pinback.domain.user.entity.User;

public interface DeleteCategoryPort {
	void deleteCategory(User user, long categoryId);
}
