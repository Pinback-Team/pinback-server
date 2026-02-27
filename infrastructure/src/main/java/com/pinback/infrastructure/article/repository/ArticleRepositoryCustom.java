package com.pinback.infrastructure.article.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.common.enums.Job;
import com.pinback.infrastructure.article.repository.dto.ArticleCountInfoV3;
import com.pinback.infrastructure.article.repository.dto.ArticleWithCountV3;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCountV2;
import com.pinback.infrastructure.article.repository.dto.SharedArticles;

public interface ArticleRepositoryCustom {
	ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable);

	ArticlesWithUnreadCount findAllByCategory(UUID userId, long articleId, Boolean isRead, Pageable pageable);

	Page<Article> findTodayRemind(UUID userId, Pageable pageable, LocalDateTime startAt, LocalDateTime endAt,
		Boolean isRead);

	RemindArticlesWithCount findTodayRemindWithCount(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt, Boolean isRead);

	ArticlesWithUnreadCount findAllByIsReadFalse(UUID userId, Pageable pageable);

	void deleteArticlesByUserIdAndCategoryId(UUID userId, long categoryId);

	RemindArticlesWithCountV2 findTodayRemindWithCountV2(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt, Boolean isReadAfterRemind);

	ArticleCountInfoV3 findTodayRemindCountV3(UUID userId, LocalDateTime startAt, LocalDateTime endAt);

	ArticleWithCountV3 findAllByReadStatus(UUID userId, Boolean readStatus, PageRequest pageRequest);

	ArticleCountInfoV3 findAllCountV3(UUID userId);

	ArticleWithCountV3 findAllByCategoryAndReadStatus(UUID userId, long categoryId, Boolean readStatus,
		PageRequest pageRequest);

	ArticleCountInfoV3 findAllCountByCategoryV3(UUID userId, long categoryId);

	SharedArticles findTopListByJob(Job job);
}
