package com.pinback.application.article.dto.response;

import java.util.List;

public record TodayRemindResponse(
	long readArticleCount,
	long unreadArticleCount,
	List<RemindArticleResponse> articles
) {
	public static TodayRemindResponse of(long readArticleCount, long unreadArticleCount, List<RemindArticleResponse> articles) {
		return new TodayRemindResponse(readArticleCount, unreadArticleCount, articles);
	}
}