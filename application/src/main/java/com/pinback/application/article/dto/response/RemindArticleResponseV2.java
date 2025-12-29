package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record RemindArticleResponseV2(
	long articleId,
	String url,
	String memo,
	LocalDateTime createdAt,
	boolean isRead,
	boolean isReadAfterRemind,
	LocalDateTime remindAt,
	CategoryResponse category
) {
	public static RemindArticleResponseV2 from(Article article) {
		return new RemindArticleResponseV2(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getCreatedAt(),
			article.isRead(),
			article.isReadAfterRemind(),
			article.getRemindAt(),
			CategoryResponse.from(article.getCategory())
		);
	}
}
