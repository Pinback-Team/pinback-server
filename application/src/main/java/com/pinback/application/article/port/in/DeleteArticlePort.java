package com.pinback.application.article.port.in;

import com.pinback.domain.user.entity.User;

public interface DeleteArticlePort {
	void deleteArticle(User user, long articleId);
}
