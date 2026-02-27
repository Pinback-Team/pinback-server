package com.pinback.application.article.dto.response;

import java.util.List;

public record GetAllArticlesResponseV3(
	long totalArticleCount,
	long unreadArticleCount,
	List<ArticleResponseV3> articles
) {
	public static GetAllArticlesResponseV3 of(
		long totalArticleCount,
		long unreadArticleCount,
		List<ArticleResponseV3> articles
	) {
		return new GetAllArticlesResponseV3(totalArticleCount, unreadArticleCount, articles);
	}
}
