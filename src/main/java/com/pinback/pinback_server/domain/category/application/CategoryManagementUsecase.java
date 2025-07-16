package com.pinback.pinback_server.domain.category.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.service.ArticleDeleteService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleGetService;
import com.pinback.pinback_server.domain.category.application.command.CategoryCreateCommand;
import com.pinback.pinback_server.domain.category.application.command.CategoryUpdateCommand;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForDashboard;
import com.pinback.pinback_server.domain.category.domain.service.CategoryDeleteService;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.category.domain.service.CategorySaveService;
import com.pinback.pinback_server.domain.category.exception.CategoryAlreadyExistException;
import com.pinback.pinback_server.domain.category.exception.CategoryLimitOverException;
import com.pinback.pinback_server.domain.category.exception.CategoryNotOwnedException;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryAllDashboardResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryAllExtensionResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryDashboardResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryExtensionResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CreateCategoryResponse;
import com.pinback.pinback_server.domain.category.presentation.dto.response.UpdateCategoryResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserGetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryManagementUsecase {
	private static final int CATEGORY_LIMIT = 10;
	private final CategorySaveService categorySaveService;
	private final CategoryGetService categoryGetService;
	private final ArticleGetService articleGetService;
	private final CategoryDeleteService categoryDeleteService;
	private final ArticleDeleteService articleDeleteService;
	private final UserGetService userGetService;

	@Transactional
	public CreateCategoryResponse createCategory(User user, CategoryCreateCommand command) {
		long existingCategoryCnt = categoryGetService.countCategoriesByUser(user);
		if (existingCategoryCnt >= CATEGORY_LIMIT) {
			throw new CategoryLimitOverException();
		}

		if (categoryGetService.checkExistsByCategoryNameAndUser(command.categoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}

		Category category = Category.create(command.categoryName(), user);
		Category savedCategory = categorySaveService.save(category);
		return CreateCategoryResponse.of(savedCategory.getId(), savedCategory.getName());
	}

	@Transactional(readOnly = true)
	public CategoryAllExtensionResponse getAllCategoriesExtension(User user) {
		Optional<Article> articleOptional = articleGetService.findRecentByUser(user);
		String recentSaved = null;
		if (articleOptional.isPresent()) {
			Article article = articleOptional.get();
			recentSaved = article.getCategory().getName();
		}
		List<Category> categories = categoryGetService.findAllForExtension(user.getId());
		List<CategoryExtensionResponse> response = categories.stream()
			.map(category -> new CategoryExtensionResponse(category.getId(), category.getName()))
			.toList();

		return CategoryAllExtensionResponse.of(recentSaved, response);
	}

	@Transactional(readOnly = true)
	public CategoryAllDashboardResponse getAllCategoriesDashboard(User user) {
		CategoriesForDashboard projection = categoryGetService.findAllForDashboard(user.getId());
		List<CategoryDashboardResponse> response = projection.getCategories().stream()
			.map(category -> new CategoryDashboardResponse(category.getId(), category.getName(),
				category.getUnreadCount()))
			.toList();

		return CategoryAllDashboardResponse.of(response);
	}

	@Transactional
	public UpdateCategoryResponse updateCategory(final User user, final Long categoryId,
		final CategoryUpdateCommand command) {
		Category category = categoryGetService.getCategoryAndUser(categoryId, user);
		if (categoryGetService.checkExistsByCategoryNameAndUser(command.categoryName(), user)) {
			throw new CategoryAlreadyExistException();
		}
		category.updateName(command.categoryName());

		return UpdateCategoryResponse.from(category);
	}

	@Transactional
	public void deleteCategory(final User user, final long categoryId) {
		Category category = categoryGetService.findById(categoryId);
		checkOwner(category, user);
		User getUser = userGetService.getUser(user.getId());
		articleDeleteService.deleteByCategory(getUser.getId(), category.getId());
		categoryDeleteService.delete(category);
	}

	public void checkOwner(Category category, User user) {
		if (!(category.getUser().equals(user))) {
			throw new CategoryNotOwnedException();
		}
	}
}
