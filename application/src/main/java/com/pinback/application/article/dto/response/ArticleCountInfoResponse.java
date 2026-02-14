package com.pinback.application.article.dto.response;

public record ArticleCountInfoResponse(
	long totalArticleCount,
	long readArticleCount,
	long unreadArticleCount
) {
	public static ArticleCountInfoResponse of(
		long totalArticleCount,
		long readArticleCount,
		long unreadArticleCount
	) {
		return new ArticleCountInfoResponse(totalArticleCount, readArticleCount, unreadArticleCount);
	}
}
