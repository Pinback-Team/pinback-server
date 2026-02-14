package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.domain.article.entity.Article;

public record CategoryArticleResponseV3(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	LocalDateTime createdAt,
	boolean isRead
) {
	public static CategoryArticleResponseV3 from(Article article) {
		return new CategoryArticleResponseV3(
			article.getId(),
			article.getUrl(),
			article.getTitle(),
			article.getThumbnail(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead()
		);
	}
}
