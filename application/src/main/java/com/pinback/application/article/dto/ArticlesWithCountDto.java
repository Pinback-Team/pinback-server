package com.pinback.application.article.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record ArticlesWithCountDto(
	long totalCount,
	long unreadCount,
	Page<Article> article
) {
}
