package com.pinback.application.article.dto.response;

import com.pinback.domain.article.entity.Article;

public record SharedArticleDetailResponse(
	long articleId,
	String ownerName,
	String memo
) {
	public static SharedArticleDetailResponse from(Article article) {
		return new SharedArticleDetailResponse(
			article.getId(),
			article.getUser().getUsername(),
			article.getMemo()
		);
	}
}
