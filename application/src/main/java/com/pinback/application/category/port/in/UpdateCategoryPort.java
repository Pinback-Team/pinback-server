package com.pinback.application.category.port.in;

import com.pinback.application.category.dto.command.UpdateCategoryCommand;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.domain.user.entity.User;

public interface UpdateCategoryPort {
	UpdateCategoryResponse updateCategory(User user, Long categoryId, UpdateCategoryCommand command);
}
