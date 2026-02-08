package com.pinback.application.category.port.in;

import com.pinback.application.category.dto.command.UpdateCategoryCommand;
import com.pinback.application.category.dto.command.UpdateCategoryCommandV3;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.application.category.dto.response.UpdateCategoryResponseV3;
import com.pinback.domain.user.entity.User;

public interface UpdateCategoryPort {
	UpdateCategoryResponse updateCategory(User user, Long categoryId, UpdateCategoryCommand command);

	UpdateCategoryResponseV3 updateCategoryV3(User user, Long categoryId, UpdateCategoryCommandV3 command);
}
