package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record RemindArticleResponseV3(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	LocalDateTime createdAt,
	boolean isRead,
	boolean isReadAfterRemind,
	LocalDateTime remindAt,
	CategoryResponse category
) {
	public static RemindArticleResponseV3 from(Article article) {
		return new RemindArticleResponseV3(
			article.getId(),
			article.getUrl(),
			article.getTitle(),
			article.getThumbnail(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead(),
			article.isReadAfterRemind(),
			article.getRemindAt(),
			CategoryResponse.from(article.getCategory())
		);
	}
}
