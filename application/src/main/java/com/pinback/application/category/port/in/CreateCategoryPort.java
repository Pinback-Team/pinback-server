package com.pinback.application.category.port.in;

import com.pinback.application.category.dto.command.CreateCategoryCommand;
import com.pinback.application.category.dto.command.CreateCategoryCommandV3;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.application.category.dto.response.CreateCategoryResponseV3;
import com.pinback.domain.user.entity.User;

public interface CreateCategoryPort {
	CreateCategoryResponse createCategory(User user, CreateCategoryCommand command);

	CreateCategoryResponseV3 createCategoryV3(User user, CreateCategoryCommandV3 command);
}
