package com.pinback.application.category.port.out;

import com.pinback.domain.category.entity.Category;

public interface CategoryDeleteServicePort {
	void delete(Category category);
}
