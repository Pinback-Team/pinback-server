package com.pinback.application.article.dto.response;

public record ReadArticleResponse(
	int acornCount,
	boolean acornCollected
) {
	public static ReadArticleResponse of(int acornCount, boolean acornCollected) {
		return new ReadArticleResponse(acornCount, acornCollected);
	}
}