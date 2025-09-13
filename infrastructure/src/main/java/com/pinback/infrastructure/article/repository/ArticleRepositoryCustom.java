package com.pinback.infrastructure.article.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pinback.domain.article.entity.Article;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCount;

public interface ArticleRepositoryCustom {
	ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable);

	ArticlesWithUnreadCount findAllByCategory(UUID userId, long articleId, boolean isRead, Pageable pageable);

	Page<Article> findTodayRemind(UUID userId, Pageable pageable, LocalDateTime startAt, LocalDateTime endAt,
		Boolean isRead);

	RemindArticlesWithCount findTodayRemindWithCount(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt, Boolean isRead);

	ArticlesWithUnreadCount findAllByIsReadFalse(UUID userId, Pageable pageable);

	void deleteArticlesByUserIdAndCategoryId(UUID userId, long categoryId);
}
