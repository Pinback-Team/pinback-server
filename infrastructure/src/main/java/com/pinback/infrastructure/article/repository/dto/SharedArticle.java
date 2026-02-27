package com.pinback.infrastructure.article.repository.dto;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.enums.CategoryColor;

public record SharedArticle(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	String ownerName,
	Long categoryId,
	String categoryName,
	CategoryColor categoryColor
) {
	public static SharedArticle from(Article article) {
		return new SharedArticle(
			article.getId(),
			article.getUrl(),
			article.getTitle(),
			article.getThumbnail(),
			article.getMemo(),
			article.getUser().getUsername(),
			article.getCategory().getId(),
			article.getCategory().getName(),
			article.getCategory().getColor()
		);
	}
}
