package com.pinback.pinback_server.domain.test.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategorySaveService;
import com.pinback.pinback_server.domain.test.presentation.dto.request.PushTestRequest;
import com.pinback.pinback_server.domain.test.presentation.dto.response.CategoriesTestResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserGetService;
import com.pinback.pinback_server.infra.firebase.FcmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUsecase {
	private final FcmService fcmService;
	private final UserGetService userGetService;
	private final CategorySaveService categorySaveService;

	public void pushTest(PushTestRequest request) {
		fcmService.sendNotification(request.fcmToken(), request.message());
	}

	public CategoriesTestResponse categoriesTest(User user) {
		// 유저 확인
		User getUser = userGetService.getUser(user.getId());

		// 카테고리 10개 생성 + 리스트에 추가
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
		List<Category> createdCategories = defaultCategoryNames.stream()
			.map(categoryName -> {
				Category category = Category.create(categoryName, user);
				return categorySaveService.save(category);
			})
			.toList();

		List<String> savedCategoryNames = createdCategories.stream()
			.map(Category::getName)
			.toList();

		return CategoriesTestResponse.of(savedCategoryNames);
	}
}
