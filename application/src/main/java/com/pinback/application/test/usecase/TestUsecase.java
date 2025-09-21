package com.pinback.application.test.usecase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pinback.application.article.port.out.ArticleDeleteServicePort;
import com.pinback.application.article.port.out.ArticleSaveServicePort;
import com.pinback.application.category.port.out.CategoryColorServicePort;
import com.pinback.application.category.port.out.CategoryDeleteServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.category.port.out.CategorySaveServicePort;
import com.pinback.application.notification.port.out.PushSubscriptionDeleteServicePort;
import com.pinback.application.test.dto.request.PushTestRequest;
import com.pinback.application.test.dto.response.CategoriesTestResponse;
import com.pinback.application.test.port.in.TestPort;
import com.pinback.application.test.port.out.FcmServicePort;
import com.pinback.application.user.port.out.UserDeleteServicePort;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUsecase implements TestPort {
	private static final int CATEGORY_LIMIT = 10;
	private final FcmServicePort fcmService;
	private final CategoryGetServicePort categoryGetServicePort;
	private final ArticleSaveServicePort articleSaveServicePort;
	private final UserGetServicePort userGetServicePort;
	private final CategorySaveServicePort categorySaveServicePort;
	private final CategoryColorServicePort categoryColorServicePort;
	private final ArticleDeleteServicePort articleDeleteServicePort;
	private final CategoryDeleteServicePort categoryDeleteServicePort;
	private final PushSubscriptionDeleteServicePort pushSubscriptionDeleteServicePort;
	private final UserDeleteServicePort userDeleteServicePort;

	@Override
	public void pushTest(PushTestRequest request) {
		fcmService.sendNotification(request.fcmToken(), request.message());
	}

	@Override
	public void createArticlesByCategory(User user, Long categoryId) {
		String format = "%s:%s";
		Category category = categoryGetServicePort.findById(categoryId);

		for (int i = 0; i < 5; i++) {
			Article article = Article.create(
				String.format(format, user.getEmail(), UUID.randomUUID()),
				"testMemo",
				user,
				category,
				null
			);
			articleSaveServicePort.save(article);
		}
	}

	@Override
	public CategoriesTestResponse createCategories(User user) {
		User getUser = userGetServicePort.findById(user.getId());
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

		List<String> createdCategoryNames = new ArrayList<>();
		Set<CategoryColor> usedColors = categoryColorServicePort.getUsedColorsByUser(getUser);

		for (int i = 0; i < 10; i++) {
			String categoryName = defaultCategoryNames.get(i % defaultCategoryNames.size());
			if (i >= defaultCategoryNames.size()) {
				categoryName = categoryName + "_" + (i - defaultCategoryNames.size() + 1);
			}

			CategoryColor availableColor = getNextAvailableColor(usedColors);
			Category category = Category.create(categoryName, getUser, availableColor);
			Category savedCategory = categorySaveServicePort.save(category);
			createdCategoryNames.add(savedCategory.getName());

			// 사용된 색상 업데이트
			usedColors.add(availableColor);
		}

		return CategoriesTestResponse.of(createdCategoryNames);
	}

	private CategoryColor getNextAvailableColor(Set<CategoryColor> usedColors) {
		return Arrays.stream(CategoryColor.values())
			.filter(color -> !usedColors.contains(color))
			.findFirst()
			.orElse(CategoryColor.COLOR1);
	}

	@Override
	public void deleteArticlesByCategory(User user, Long categoryId) {
		Category category = categoryGetServicePort.findById(categoryId);
		User getUser = userGetServicePort.findById(user.getId());
		articleDeleteServicePort.deleteByCategory(getUser, category.getId());
	}

	@Override
	public void deleteUser(User user) {
		User getUser = userGetServicePort.findById(user.getId());
		articleDeleteServicePort.deleteAllByUser(getUser);
		categoryDeleteServicePort.deleteAllByUser(getUser);
		pushSubscriptionDeleteServicePort.deleteByUser(getUser);
		userDeleteServicePort.delete(getUser);
	}
}
