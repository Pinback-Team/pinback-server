package com.pinback.infrastructure.article.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record ArticlesWithUnreadCount(
	Long unReadCount,
	Page<Article> article) {
}
