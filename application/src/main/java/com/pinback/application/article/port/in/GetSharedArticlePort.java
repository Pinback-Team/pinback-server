package com.pinback.application.article.port.in;

import com.pinback.application.article.dto.response.SharedArticleDetailResponse;
import com.pinback.application.article.dto.response.SharedArticlePageResponse;
import com.pinback.domain.user.entity.User;

public interface GetSharedArticlePort {
	SharedArticlePageResponse getAllSharedArticle(User user);

	SharedArticleDetailResponse getSharedArticleDetail(User user, Long articleId);
}
