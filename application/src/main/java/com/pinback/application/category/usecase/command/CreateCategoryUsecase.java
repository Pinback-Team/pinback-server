package com.pinback.application.category.usecase.command;

import java.util.Arrays;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.command.CreateCategoryCommand;
import com.pinback.application.category.dto.response.CreateCategoryResponse;
import com.pinback.application.category.port.in.CreateCategoryPort;
import com.pinback.application.category.port.out.CategoryColorServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.category.port.out.CategorySaveServicePort;
import com.pinback.application.common.exception.CategoryAlreadyExistException;
import com.pinback.application.common.exception.CategoryLimitOverException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.category.exception.CategoryNameLengthOverException;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCategoryUsecase implements CreateCategoryPort {

	private final CategorySaveServicePort categorySaveService;
	private final CategoryGetServicePort categoryGetService;
	private final CategoryColorServicePort categoryColorService;

	@Override
	public CreateCategoryResponse createCategory(User user, CreateCategoryCommand command) {
		validateCategoryCreation(user, command);

		CategoryColor availableColor = getNextAvailableColor(user);
		try {
			Category category = Category.create(command.categoryName(), user, availableColor);
			Category savedCategory = categorySaveService.save(category);
			return CreateCategoryResponse.of(savedCategory.getId(), savedCategory.getName(), savedCategory.getColor());
		} catch (CategoryNameLengthOverException e) {
			throw new CategoryNameLengthOverException();
		}
	}

	private void validateCategoryCreation(User user, CreateCategoryCommand command) {
		if (categoryGetService.countCategoriesByUser(user) >= 10) {
			throw new CategoryLimitOverException();
		}
		if (categoryGetService.checkExistsByCategoryNameAndUser(command.categoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}
	}

	private CategoryColor getNextAvailableColor(User user) {
		Set<CategoryColor> usedColors = categoryColorService.getUsedColorsByUser(user);

		return Arrays.stream(CategoryColor.values())
			.filter(color -> !usedColors.contains(color))
			.findFirst()
			.orElse(CategoryColor.COLOR1);
	}
}
