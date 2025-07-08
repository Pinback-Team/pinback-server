package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.util.List;

public record ArticleAllResponse(
	long totalArticle,
	long totalUnreadArticle,
	List<ArticlesResponse> articles
) {
	public static ArticleAllResponse of(long totalArticle, long totalUnreadArticle, List<ArticlesResponse> articles) {
		return new ArticleAllResponse(
			totalArticle,
			totalUnreadArticle,
			articles
		);
	}
}
