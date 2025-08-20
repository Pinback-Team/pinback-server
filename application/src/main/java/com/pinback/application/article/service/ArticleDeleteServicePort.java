package com.pinback.application.article.service;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

public interface ArticleDeleteServicePort {
	void deleteByCategory(User user, long categoryId);

	void delete(Article article);
}
