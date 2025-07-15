package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.util.List;

public record ArticleUnreadResponse(
	long totalUnreadArticle,
	List<ArticlesResponse> articles
) {
	public static ArticleUnreadResponse of(long totalUnreadArticle, List<ArticlesResponse> articles) {
		return new ArticleUnreadResponse(
			totalUnreadArticle,
			articles
		);
	}
}
