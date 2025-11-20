package com.pinback.application.article.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record RemindArticlesWithCountDtoV2(
	boolean hasNext,
	long readCount,
	long unreadCount,
	long totalCount,
	Page<Article> articles
) {
}
