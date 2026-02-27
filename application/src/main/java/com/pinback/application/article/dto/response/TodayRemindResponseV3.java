package com.pinback.application.article.dto.response;

import java.util.List;

public record TodayRemindResponseV3(
	boolean hasNext,
	long totalArticleCount,
	long readArticleCount,
	long unreadArticleCount,
	List<RemindArticleResponseV3> articles
) {
	public static TodayRemindResponseV3 of(
		boolean hasNext,
		long totalArticleCount,
		long readArticleCount,
		long unreadArticleCount,
		List<RemindArticleResponseV3> articles
	) {
		return new TodayRemindResponseV3(hasNext, totalArticleCount, readArticleCount, unreadArticleCount, articles);
	}
}
