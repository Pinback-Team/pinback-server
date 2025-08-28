package com.pinback.application.article.port.in;

import com.pinback.application.article.dto.command.ArticleUpdateCommand;
import com.pinback.domain.user.entity.User;

public interface UpdateArticlePort {
	void updateArticle(User user, long articleId, ArticleUpdateCommand command);
}
