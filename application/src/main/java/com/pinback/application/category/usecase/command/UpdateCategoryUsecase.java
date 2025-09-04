package com.pinback.application.category.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.command.UpdateCategoryCommand;
import com.pinback.application.category.dto.response.UpdateCategoryResponse;
import com.pinback.application.category.port.in.UpdateCategoryPort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.exception.CategoryNameLengthOverException;
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
		try {
			category.updateName(command.categoryName());
		} catch (CategoryNameLengthOverException e) {
			throw new CategoryNameLengthOverException();
		}

		return UpdateCategoryResponse.from(category);
	}
}
