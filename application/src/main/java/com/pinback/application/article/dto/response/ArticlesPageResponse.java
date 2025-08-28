package com.pinback.application.article.dto.response;

import java.util.List;

public record ArticlesPageResponse(
	long totalArticle,
	long totalUnreadArticle,
	List<ArticleResponse> articles
) {
	public static ArticlesPageResponse of(long totalArticle, long totalUnreadArticle, List<ArticleResponse> articles) {
		return new ArticlesPageResponse(totalArticle, totalUnreadArticle, articles);
	}
}
