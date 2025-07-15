package com.pinback.pinback_server.domain.article.domain.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;

public interface ArticleRepositoryCustom {
	ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable);

	ArticlesWithUnreadCount findAllByCategory(UUID userId, long articleId, Pageable pageable);

	Page<Article> findTodayRemind(UUID userId, Pageable pageable, LocalDateTime startAt, LocalDateTime endAt);

	ArticlesWithUnreadCount findAllByIsReadFalse(UUID userId, Pageable pageable);
}
