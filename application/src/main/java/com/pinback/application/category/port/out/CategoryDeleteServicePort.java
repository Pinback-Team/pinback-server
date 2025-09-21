package com.pinback.application.category.port.out;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface CategoryDeleteServicePort {
	void delete(Category category);

	void deleteAllByUser(User user);
}
