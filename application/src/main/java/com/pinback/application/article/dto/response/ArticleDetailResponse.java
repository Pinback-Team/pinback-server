package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.domain.article.entity.Article;

public record ArticleDetailResponse(
	long id,
	String url,
	String memo,
	LocalDateTime remindAt,
	LocalDateTime createdAt
) {
	public static ArticleDetailResponse from(Article article) {
		return new ArticleDetailResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getRemindAt(),
			article.getCreatedAt()
		);
	}
}
