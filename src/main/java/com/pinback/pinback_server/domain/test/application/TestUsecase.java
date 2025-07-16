package com.pinback.pinback_server.domain.test.application;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.service.ArticleDeleteService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.category.domain.service.CategorySaveService;
import com.pinback.pinback_server.domain.category.exception.CategoryLimitOverException;
import com.pinback.pinback_server.domain.test.presentation.dto.request.PushTestRequest;
import com.pinback.pinback_server.domain.test.presentation.dto.response.CategoriesTestResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserGetService;
import com.pinback.pinback_server.infra.firebase.FcmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUsecase {
	private static final int CATEGORY_LIMIT = 10;
	private final FcmService fcmService;
	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;
	private final UserGetService userGetService;
	private final CategorySaveService categorySaveService;
	private final ArticleDeleteService articleDeleteService;

	public void pushTest(PushTestRequest request) {
		fcmService.sendNotification(request.fcmToken(), request.message());
	}

	public void createByCategory(User user, long categoryId) {
		String format = "%s:%s";
		Category category = categoryGetService.findById(categoryId);

		for (int i = 0; i < 5; i++) {
			articleSaveService.save(Article.create(
				String.format(format, user.getEmail(), UUID.randomUUID()), "testMemo", user, category, null
			));
		}
	}

	public CategoriesTestResponse categoriesTest(User user) {
		User getUser = userGetService.getUser(user.getId());
		List<String> defaultCategoryNames = Arrays.asList(
			"집",
			"취업",
			"동아리",
			"자기계발",
			"포트폴리오",
			"경제시사흐름",
			"최신기술트렌드",
			"인성직무면접꿀팁",
			"어학자격증취득준비",
			"멘탈관리스트레스해소"
		);

		long existingCategoryCnt = categoryGetService.countCategoriesByUser(getUser);
		if (existingCategoryCnt >= CATEGORY_LIMIT) {
			throw new CategoryLimitOverException();
		}

		List<Category> createdCategories = defaultCategoryNames.stream()
			.map(categoryName -> {
				Category category = Category.create(categoryName, getUser);
				return categorySaveService.save(category);
			})
			.toList();

		List<String> savedCategoryNames = createdCategories.stream()
			.map(Category::getName)
			.toList();

		return CategoriesTestResponse.of(savedCategoryNames);
	}

	public void deleteByCategory(User user, long categoryId) {
		Category category = categoryGetService.findById(categoryId);
		User getUser = userGetService.getUser(user.getId());
		articleDeleteService.deleteByCategory(getUser.getId(), category.getId());
	}
}
