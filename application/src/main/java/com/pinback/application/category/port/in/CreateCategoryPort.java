package com.pinback.application.category.port.in;

import com.pinback.application.category.dto.command.CreateCategoryCommand;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.domain.user.entity.User;

public interface CreateCategoryPort {
	CreateCategoryResponse createCategory(User user, CreateCategoryCommand command);
}
