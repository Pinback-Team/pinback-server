package com.pinback.pinback_server.domain.article.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;
import com.pinback.pinback_server.domain.article.exception.ArticleNotFoundException;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleGetService {

	private final ArticleRepository articleRepository;

	public boolean checkExistsByUserAndUrl(User user, String url) {
		return articleRepository.existsByUserAndUrl(user, url);
	}

	public Article findById(long articleId) {
		return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
	}

	public ArticlesWithUnreadCount findAll(UUID userId, PageRequest pageRequest) {
		return articleRepository.findAllCustom(userId, pageRequest);
	}

	public ArticlesWithUnreadCount findAllByCategory(UUID userId, Category category, PageRequest pageRequest) {
		return articleRepository.findAllByCategory(userId, category.getId(), pageRequest);
	}

	// feat/#23 브랜치에서 추가된 메서드
	public Page<Article> findTodayRemind(UUID userId, LocalDateTime today, Pageable pageable) {
		LocalDateTime startAt = today.minusDays(1L).plusMinutes(1L);
		return articleRepository.findTodayRemind(userId, pageable, startAt, today);
	}

	// dev 브랜치에서 추가된 메서드
	public Optional<Article> findRecentByUser(User user) {
		return articleRepository.findRecentArticleByUser(user);
	}
}
