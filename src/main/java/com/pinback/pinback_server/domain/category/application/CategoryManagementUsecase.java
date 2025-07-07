package com.pinback.pinback_server.domain.category.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.category.application.command.CategoryCreateCommand;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.category.domain.service.CategorySaveService;
import com.pinback.pinback_server.domain.category.exception.CategoryAlreadyExistException;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CreateCategoryResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryManagementUsecase {
	private final CategorySaveService categorySaveService;
	private final CategoryGetService categoryGetService;

	@Transactional
	public CreateCategoryResponse createCategory(User user, CategoryCreateCommand command) {
		if (categoryGetService.checkExistsByCategoryNameAndUser(command.CategoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}
		Category category = Category.create(command.CategoryName(), user);
		Category savedCategory = categorySaveService.save(category);
		return CreateCategoryResponse.of(savedCategory.getId(), savedCategory.getName());
	}
}
