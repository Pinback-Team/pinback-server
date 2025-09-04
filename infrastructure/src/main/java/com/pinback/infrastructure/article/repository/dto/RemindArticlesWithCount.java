package com.pinback.infrastructure.article.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record RemindArticlesWithCount(
	long readCount,
	long unreadCount,
	Page<Article> articles
) {
}