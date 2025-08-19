package com.pinback.application.category.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.service.ArticleDeleteService;
import com.pinback.application.article.service.ArticleGetService;
import com.pinback.application.category.service.CategoryDeleteService;
import com.pinback.application.category.service.CategoryGetService;
import com.pinback.application.category.service.CategorySaveService;
import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.application.common.exception.CategoryAlreadyExistException;
import com.pinback.application.common.exception.CategoryLimitOverException;
import com.pinback.application.common.exception.CategoryNotOwnedException;
import com.pinback.domain.user.entity.User;

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
		CategoriesForDashboardDto projection = categoryGetService.findAllForDashboard(user.getId());
		List<CategoryDashboardResponse> response = projection.categories().stream()
			.map(category -> new CategoryDashboardResponse(category.id(), category.name(),
				category.unreadCount()))
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
		articleDeleteService.deleteByCategory(user, category.getId());
		categoryDeleteService.delete(category);
	}

	public void checkOwner(Category category, User user) {
		if (!(category.getUser().equals(user))) {
			throw new CategoryNotOwnedException();
		}
	}

	public record CategoryCreateCommand(String categoryName) {}
	
	public record CategoryUpdateCommand(String categoryName) {}

	public static class CreateCategoryResponse {
		public static CreateCategoryResponse of(Long id, String name) {
			// Implementation
			return null;
		}
	}

	public static class CategoryAllExtensionResponse {
		public static CategoryAllExtensionResponse of(String recentSaved, List<CategoryExtensionResponse> responses) {
			// Implementation
			return null;
		}
	}

	public static class CategoryExtensionResponse {
		public CategoryExtensionResponse(Long id, String name) {
			// Implementation
		}
	}

	public static class CategoryAllDashboardResponse {
		public static CategoryAllDashboardResponse of(List<CategoryDashboardResponse> responses) {
			// Implementation
			return null;
		}
	}

	public static class CategoryDashboardResponse {
		public CategoryDashboardResponse(Long id, String name, long unreadCount) {
			// Implementation
		}
	}

	public static class UpdateCategoryResponse {
		public static UpdateCategoryResponse from(Category category) {
			// Implementation
			return null;
		}
	}

}