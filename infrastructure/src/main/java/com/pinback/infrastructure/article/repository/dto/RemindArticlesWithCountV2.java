package com.pinback.infrastructure.article.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record RemindArticlesWithCountV2(
	long readCount,
	long unreadCount,
	long totalCount,
	Page<Article> articles
) {
}
