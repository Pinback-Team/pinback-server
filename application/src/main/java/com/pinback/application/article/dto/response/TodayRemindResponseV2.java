package com.pinback.application.article.dto.response;

import java.util.List;

public record TodayRemindResponseV2(
	boolean hasNext,
	long totalArticleCount,
	long readArticleCount,
	long unreadArticleCount,
	List<RemindArticleResponseV2> articles
) {
	public static TodayRemindResponseV2 of(
		boolean hasNext,
		long totalArticleCount,
		long readArticleCount,
		long unreadArticleCount,
		List<RemindArticleResponseV2> articles
	) {
		return new TodayRemindResponseV2(hasNext, totalArticleCount, readArticleCount, unreadArticleCount, articles);
	}
}
