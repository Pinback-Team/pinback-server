package com.pinback.application.article.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record RemindArticlesWithCountDto(
	long readCount,
	long unreadCount,
	Page<Article> articles
) {
}