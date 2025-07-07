package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.util.List;

public record ArticleAllResponse(
	long totalArticle,
	List<ArticlesResponse> articles
) {
	public static ArticleAllResponse of(long totalArticle, List<ArticlesResponse> articles) {
		return new ArticleAllResponse(
			totalArticle,
			articles
		);
	}
}
