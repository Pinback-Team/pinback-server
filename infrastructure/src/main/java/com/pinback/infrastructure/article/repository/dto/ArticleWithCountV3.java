package com.pinback.infrastructure.article.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record ArticleWithCountV3(
	Long totalCount,
	Long unreadCount,
	Page<Article> article
) {
}
