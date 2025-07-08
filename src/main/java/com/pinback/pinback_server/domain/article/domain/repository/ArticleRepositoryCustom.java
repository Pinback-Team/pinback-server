package com.pinback.pinback_server.domain.article.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;

public interface ArticleRepositoryCustom {
	ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable);

	ArticlesWithUnreadCount findAllByCategory(UUID userId, long articleId, Pageable pageable);
}
