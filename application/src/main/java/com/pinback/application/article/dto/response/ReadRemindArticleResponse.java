package com.pinback.application.article.dto.response;

public record ReadRemindArticleResponse(
	boolean isReadAfterRemind,
	int finalAcornCount,
	boolean isCollected
) {
	public static ReadRemindArticleResponse of(boolean readAfterRemind, int finalAcornCount, boolean isCollected) {
		return new ReadRemindArticleResponse(readAfterRemind, finalAcornCount, isCollected);
	}
}
