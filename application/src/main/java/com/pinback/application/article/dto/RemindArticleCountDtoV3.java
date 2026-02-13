package com.pinback.application.article.dto;

public record RemindArticleCountDtoV3(
	long totalCount,
	long readCount,
	long unreadCount
) {
}
