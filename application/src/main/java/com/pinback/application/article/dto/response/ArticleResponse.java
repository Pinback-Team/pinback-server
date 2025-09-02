package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record ArticleResponse(
	long articleId,
	String url,
	String memo,
	LocalDateTime createdAt,
	boolean isRead,
	CategoryResponse category
) {
	public static ArticleResponse from(Article article) {
		return new ArticleResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead(),
			CategoryResponse.from(article.getCategory())
		);
	}
}
