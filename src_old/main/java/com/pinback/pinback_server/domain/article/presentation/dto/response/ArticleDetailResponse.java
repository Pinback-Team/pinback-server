package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.category.presentation.dto.response.CategoryDetail;

public record ArticleDetailResponse(
	long articleId,
	String url,
	String memo,
	LocalDateTime remindAt,
	CategoryDetail category
) {
	public static ArticleDetailResponse from(Article article) {
		CategoryDetail categoryDetail = CategoryDetail.of(article.getCategory());
		return new ArticleDetailResponse(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getRemindAt(),
			categoryDetail
		);
	}
}
