package com.pinback.application.article.port.in;

import com.pinback.application.article.dto.command.ArticleCreateCommand;
import com.pinback.domain.user.entity.User;

public interface CreateArticlePort {
	void createArticle(User user, ArticleCreateCommand command);
}
