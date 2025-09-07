package com.pinback.application.article.dto.response;

import java.util.List;

public record GetAllArticlesResponse(
	long totalArticle,
	long totalUnreadArticle,
	boolean isNewUser,
	List<ArticleResponse> articles
) {
	public static GetAllArticlesResponse of(long totalArticle, long totalUnreadArticle, boolean isNewUser, List<ArticleResponse> articles) {
		return new GetAllArticlesResponse(totalArticle, totalUnreadArticle, isNewUser, articles);
	}
}