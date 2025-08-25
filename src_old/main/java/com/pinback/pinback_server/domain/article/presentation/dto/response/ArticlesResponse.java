package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;

import com.pinback.pinback_server.domain.article.domain.entity.Article;

public record ArticlesResponse(
	long articleId,
	String url,
	String memo,
	LocalDateTime createdAt,
	boolean isRead
) {
	public static ArticlesResponse from(Article article) {
		return new ArticlesResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead()
		);
	}
}
