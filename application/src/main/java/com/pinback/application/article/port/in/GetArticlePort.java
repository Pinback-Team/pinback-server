package com.pinback.application.article.port.in;

import java.time.LocalDateTime;

import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.RemindArticlesResponse;
import com.pinback.domain.user.entity.User;

public interface GetArticlePort {
	ArticleDetailResponse getArticleDetail(long articleId);

	ArticleDetailResponse checkArticleExists(User user, String url);

	ArticlesPageResponse getAllArticles(User user, PageQuery query);

	ArticlesPageResponse getAllArticlesByCategory(User user, long categoryId, PageQuery query);

	ArticlesPageResponse getUnreadArticles(User user, PageQuery query);

	RemindArticlesResponse getRemindArticles(User user, LocalDateTime now, PageQuery query);
}
