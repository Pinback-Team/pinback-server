package com.pinback.application.article.dto.response;

import com.pinback.application.article.dto.SharedArticleDto;
import com.pinback.application.category.dto.response.CategoryResponse;

public record SharedArticleResponse(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	String ownerName,
	CategoryResponse category
) {
	public static SharedArticleResponse from(SharedArticleDto dto) {
		return new SharedArticleResponse(
			dto.articleId(),
			dto.url(),
			dto.title(),
			dto.thumbnailUrl(),
			dto.memo(),
			dto.ownerName(),
			CategoryResponse.of(dto.categoryId(), dto.categoryName(), dto.categoryColor().toString())
		);
	}
}
