package com.pinback.application.article.dto.response;

import java.util.List;

public record ArticlesPageResponseV3(
	long totalArticleCount,
	long unreadArticleCount,
	String categoryName,
	List<CategoryArticleResponseV3> articles
) {
	public static ArticlesPageResponseV3 of(long totalArticleCount, long unreadArticleCount, String categoryName,
		List<CategoryArticleResponseV3> articles) {
		return new ArticlesPageResponseV3(totalArticleCount, unreadArticleCount, categoryName, articles);
	}
}
