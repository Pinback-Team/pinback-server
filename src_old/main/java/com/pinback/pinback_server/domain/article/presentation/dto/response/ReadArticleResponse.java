package com.pinback.pinback_server.domain.article.presentation.dto.response;

public record ReadArticleResponse(
	int finalAcornCount,
	boolean isCollected
) {

	public static ReadArticleResponse of(int finalAcornCount, boolean isCollected) {
		return new ReadArticleResponse(finalAcornCount, isCollected);
	}
}
