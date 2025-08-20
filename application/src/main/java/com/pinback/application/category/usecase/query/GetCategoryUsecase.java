package com.pinback.application.category.usecase.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.service.ArticleGetService;
import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.application.category.dto.response.CategoriesForDashboardResponse;
import com.pinback.application.category.dto.response.CategoriesForExtensionResponse;
import com.pinback.application.category.dto.response.CategoryDashboardResponse;
import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCategoryUsecase implements GetCategoryPort {

	private final CategoryGetServicePort categoryGetServicePort;
	private final ArticleGetService articleGetService;

	@Override
	public CategoriesForExtensionResponse getAllCategoriesForExtension(User user) {
		Optional<Article> articleOptional = articleGetService.findRecentByUser(user);
		String recentSaved = articleOptional
			.map(article -> article.getCategory().getName())
			.orElse(null);

		List<Category> categories = categoryGetServicePort.findAllForExtension(user.getId());
		List<CategoryResponse> categoryResponses = categories.stream()
			.map(CategoryResponse::from)
			.toList();

		return CategoriesForExtensionResponse.of(recentSaved, categoryResponses);
	}

	@Override
	public CategoriesForDashboardResponse getAllCategoriesForDashboard(User user) {
		CategoriesForDashboardDto projection = categoryGetServicePort.findAllForDashboard(user.getId());

		List<CategoryDashboardResponse> categoryResponses = projection.categories().stream()
			.map(category -> new CategoryDashboardResponse(
				category.id(),
				category.name(),
				category.unreadCount()
			))
			.toList();

		return CategoriesForDashboardResponse.of(categoryResponses);
	}
}
