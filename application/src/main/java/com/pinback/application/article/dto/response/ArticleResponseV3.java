package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record ArticleResponseV3(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	LocalDateTime createdAt,
	boolean isRead,
	CategoryResponse category
) {
	public static ArticleResponseV3 from(Article article) {
		return new ArticleResponseV3(
			article.getId(),
			article.getUrl(),
			article.getTitle(),
			article.getThumbnail(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead(),
			CategoryResponse.from(article.getCategory())
		);
	}
}
