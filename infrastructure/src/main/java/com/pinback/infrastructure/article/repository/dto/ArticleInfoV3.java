package com.pinback.infrastructure.article.repository.dto;

public record ArticleInfoV3(
	Long totalCount,
	Long unreadCount
) {
}
