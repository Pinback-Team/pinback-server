package com.pinback.pinback_server.domain.article.presentation.dto.response;

import com.pinback.pinback_server.domain.article.domain.entity.Article;

public record ArticlesResponse(
	long articleId,
	String url,
	String memo
) {
	public static ArticlesResponse from(Article article) {
		return new ArticlesResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo()
		);
	}
}
