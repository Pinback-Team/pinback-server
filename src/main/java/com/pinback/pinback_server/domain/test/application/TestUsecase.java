package com.pinback.pinback_server.domain.test.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.test.presentation.dto.request.PushTestRequest;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.infra.firebase.FcmService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUsecase {
	private final FcmService fcmService;
	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;

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

}
