package com.pinback.application.article.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;

public record ArticlesWithUnreadCountDto(
	Long unReadCount,
	Page<Article> article
) {
}
