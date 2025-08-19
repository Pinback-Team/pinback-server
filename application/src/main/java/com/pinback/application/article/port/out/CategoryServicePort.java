package com.pinback.application.article.port.out;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface CategoryServicePort {
	Category getCategoryAndUser(long categoryId, User user);
}
