package com.pinback.infrastructure.article.repository.dto;

public record ArticleCountInfoV3(
	long totalCount,
	long readCount,
	long unreadCount
) {
}
