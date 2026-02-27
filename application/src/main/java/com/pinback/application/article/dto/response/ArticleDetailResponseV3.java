package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;

import com.pinback.application.category.dto.response.CategoryResponse;
import com.pinback.domain.article.entity.Article;

public record ArticleDetailResponseV3(
	long articleId,
	String url,
	String title,
	String thumbnailUrl,
	String memo,
	LocalDateTime remindAt,
	LocalDateTime createdAt,
	CategoryResponse categoryResponse
) {
	public static ArticleDetailResponseV3 from(Article article) {
		return new ArticleDetailResponseV3(
			article.getId(),
			article.getUrl(),
			article.getTitle(),
			article.getThumbnail(),
			article.getMemo(),
			article.getRemindAt(),
			article.getCreatedAt(),
			CategoryResponse.from(article.getCategory())
		);
	}

}
