package com.pinback.application.article.port.in;

import java.time.LocalDateTime;

import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticleDetailResponseV3;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.GetAllArticlesResponse;
import com.pinback.application.article.dto.response.TodayRemindResponse;
import com.pinback.application.article.dto.response.TodayRemindResponseV2;
import com.pinback.domain.user.entity.User;

public interface GetArticlePort {
	ArticleDetailResponse getArticleDetail(long articleId);

	ArticleDetailResponse checkArticleExists(User user, String url);

	GetAllArticlesResponse getAllArticles(User user, PageQuery query);

	ArticlesPageResponse getAllArticlesByCategory(User user, long categoryId, Boolean isRead, PageQuery query);

	ArticlesPageResponse getUnreadArticles(User user, PageQuery query);

	TodayRemindResponse getRemindArticles(User user, LocalDateTime now, boolean readStatus, PageQuery query);

	TodayRemindResponseV2 getRemindArticlesV2(User user, LocalDateTime now, boolean readStatus, PageQuery query);

	ArticleDetailResponseV3 getArticleDetailWithMetadata(long articleId);
}
