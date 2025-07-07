package com.pinback.pinback_server.domain.article.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleManagementUsecase {

	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;

	//TODO: 리마인드 로직 추가 필요
	public void createArticle(User user, ArticleCreateCommand command) {
		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category);
		articleSaveService.save(article);
	}
}
