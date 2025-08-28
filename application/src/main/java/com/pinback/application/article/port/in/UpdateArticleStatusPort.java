package com.pinback.application.article.port.in;

import com.pinback.application.article.dto.response.ReadArticleResponse;
import com.pinback.domain.user.entity.User;

public interface UpdateArticleStatusPort {
	ReadArticleResponse updateArticleStatus(User user, long articleId);
}
