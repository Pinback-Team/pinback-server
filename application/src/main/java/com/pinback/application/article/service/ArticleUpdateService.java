package com.pinback.application.article.service;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

public interface ArticleUpdateService {
	Article findByUserAndId(User user, long articleId);
}
