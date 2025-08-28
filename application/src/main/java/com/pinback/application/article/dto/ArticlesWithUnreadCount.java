package com.pinback.application.article.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record ArticlesWithUnreadCount(
	Page<Article> articles,
	long unreadCount
) {
}
