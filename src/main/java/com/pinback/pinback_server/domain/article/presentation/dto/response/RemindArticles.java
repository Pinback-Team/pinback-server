package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;

import com.pinback.pinback_server.domain.article.domain.entity.Article;

public record RemindArticles(
	long articleId,
	String url,
	String memo,
	LocalDateTime remindAt,
	boolean isRead
) {
	public static RemindArticles from(Article article) {
		return new RemindArticles(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getRemindAt(),
			article.isRead()
		);
	}
}
