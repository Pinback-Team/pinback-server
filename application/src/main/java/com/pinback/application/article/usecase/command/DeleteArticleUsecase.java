package com.pinback.application.article.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.port.in.DeleteArticlePort;
import com.pinback.application.article.service.ArticleDeleteServicePort;
import com.pinback.application.article.service.ArticleGetServicePort;
import com.pinback.application.common.exception.ArticleNotOwnedException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteArticleUsecase implements DeleteArticlePort {

	private final ArticleGetServicePort articleGetService;
	private final ArticleDeleteServicePort articleDeleteService;

	@Override
	public void deleteArticle(User user, long articleId) {
		Article article = articleGetService.findById(articleId);
		validateOwnership(article, user);
		articleDeleteService.delete(article);
	}

	private void validateOwnership(Article article, User user) {
		if (!article.getUser().equals(user)) {
			throw new ArticleNotOwnedException();
		}
	}
}
