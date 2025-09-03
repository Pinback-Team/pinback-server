package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record RemindArticleResponse(
	long articleId,
	String url,
	String memo,
	LocalDateTime createdAt,
	boolean isRead,
	LocalDateTime remindAt,
	CategoryResponse category
) {
	public static RemindArticleResponse from(Article article) {
		return new RemindArticleResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead(),
			article.getRemindAt(),
			CategoryResponse.from(article.getCategory())
		);
	}
}