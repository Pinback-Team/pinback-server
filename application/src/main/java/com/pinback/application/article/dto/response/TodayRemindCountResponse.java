package com.pinback.application.article.dto.response;

public record TodayRemindCountResponse(
	long totalArticleCount,
	long readArticleCount,
	long unreadArticleCount
) {
	public static TodayRemindCountResponse of(
		long totalArticleCount,
		long readArticleCount,
		long unreadArticleCount
	) {
		return new TodayRemindCountResponse(totalArticleCount, readArticleCount, unreadArticleCount);
	}
}
