package com.pinback.application.article.dto.response;

public record ArticleMetadataResponse(
	String title,
	String thumbnailUrl
) {
	public static ArticleMetadataResponse of(String title, String thumbnailUrl) {
		return new ArticleMetadataResponse(title, thumbnailUrl);
	}
}
