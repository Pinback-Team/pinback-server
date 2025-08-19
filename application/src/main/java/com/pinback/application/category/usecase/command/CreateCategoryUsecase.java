package com.pinback.application.category.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.command.CreateCategoryCommand;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.application.category.port.in.CreateCategoryPort;
import com.pinback.application.category.port.out.CategoryRepositoryPort;
import com.pinback.domain.category.entity.Category;
import com.pinback.application.common.exception.CategoryAlreadyExistException;
import com.pinback.application.common.exception.CategoryLimitOverException;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCategoryUsecase implements CreateCategoryPort {
	
	private static final int CATEGORY_LIMIT = 10;
	
	private final CategoryRepositoryPort categoryRepositoryPort;

	@Override
	public CreateCategoryResponse createCategory(User user, CreateCategoryCommand command) {
		validateCategoryCreation(user, command);
		
		Category category = Category.create(command.categoryName(), user);
		Category savedCategory = categoryRepositoryPort.save(category);
		
		return CreateCategoryResponse.of(savedCategory.getId(), savedCategory.getName());
	}
	
	private void validateCategoryCreation(User user, CreateCategoryCommand command) {
		long existingCategoryCnt = categoryRepositoryPort.countCategoriesByUser(user);
		if (existingCategoryCnt >= CATEGORY_LIMIT) {
			throw new CategoryLimitOverException();
		}
		
		if (categoryRepositoryPort.checkExistsByCategoryNameAndUser(command.categoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}
	}
}