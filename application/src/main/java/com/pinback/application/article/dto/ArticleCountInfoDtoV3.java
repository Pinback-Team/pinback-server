package com.pinback.application.article.dto;

public record ArticleCountInfoDtoV3(
	long totalCount,
	long readCount,
	long unreadCount
) {
}
