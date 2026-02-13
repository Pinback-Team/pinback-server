package com.pinback.infrastructure.article.repository.dto;

public record RemindArticleCountV3(
	long totalCount,
	long readCount,
	long unreadCount
) {
}
