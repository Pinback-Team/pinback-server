package com.pinback.application.category.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.command.UpdateCategoryCommand;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.application.category.port.in.UpdateCategoryPort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.CategoryAlreadyExistException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCategoryUsecase implements UpdateCategoryPort {

	private final CategoryGetServicePort categoryGetService;

	@Override
	public UpdateCategoryResponse updateCategory(User user, Long categoryId, UpdateCategoryCommand command) {
		Category category = categoryGetService.getCategoryAndUser(categoryId, user);

		if (categoryGetService.checkExistsByCategoryNameAndUser(command.categoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}

		category.updateName(command.categoryName());

		return UpdateCategoryResponse.from(category);
	}
}
