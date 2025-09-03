package com.pinback.infrastructure.article.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.common.exception.ArticleNotFoundException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.article.repository.ArticleRepository;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleGetService implements ArticleGetServicePort {

	private final ArticleRepository articleRepository;

	@Override
	public Optional<Article> findRecentByUser(User user) {
		return articleRepository.findRecentArticleByUser(user);
	}

	@Override
	public boolean checkExistsByUserAndUrl(User user, String url) {
		return articleRepository.existsByUserAndUrl(user, url);
	}

	@Override
	public Article findById(long articleId) {
		return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
	}

	@Override
	public Optional<Article> findByUrlAndUser(User user, String url) {
		return articleRepository.findArticleByUserAndUrl(user, url);
	}

	@Override
	public ArticlesWithUnreadCountDto findAll(User user, PageRequest pageRequest) {
		ArticlesWithUnreadCount infraResult = articleRepository.findAllCustom(user.getId(), pageRequest);
		return convertToDto(infraResult);
	}

	@Override
	public ArticlesWithUnreadCountDto findAllByCategory(User user, Category category, PageRequest pageRequest) {
		ArticlesWithUnreadCount infraResult = articleRepository.findAllByCategory(user.getId(), category.getId(),
			pageRequest);
		return convertToDto(infraResult);
	}

	@Override
	public ArticlesWithUnreadCountDto findUnreadArticles(User user, PageRequest pageRequest) {
		ArticlesWithUnreadCount infraResult = articleRepository.findAllByIsReadFalse(user.getId(), pageRequest);
		return convertToDto(infraResult);
	}

	@Override
	public Article findByUserAndId(User user, long articleId) {
		return articleRepository.findArticleByUserAndId(user, articleId).orElseThrow(ArticleNotFoundException::new);
	}

	@Override
	public Page<Article> findTodayRemind(User user, LocalDateTime remindDateTime, Pageable pageable, Boolean isRead) {
		return articleRepository.findTodayRemind(user.getId(), pageable, remindDateTime, remindDateTime.plusDays(1), isRead);
	}

	@Override
	public RemindArticlesWithCountDto findTodayRemindWithCount(User user, LocalDateTime remindDateTime, Pageable pageable, Boolean isRead) {
		RemindArticlesWithCount infraResult = articleRepository.findTodayRemindWithCount(user.getId(), pageable, remindDateTime, remindDateTime.plusDays(1), isRead);
		return new RemindArticlesWithCountDto(
			infraResult.readCount(),
			infraResult.unreadCount(),
			infraResult.articles()
		);
	}

	private ArticlesWithUnreadCountDto convertToDto(ArticlesWithUnreadCount infraResult) {
		return new ArticlesWithUnreadCountDto(
			infraResult.unReadCount(),
			infraResult.article()
		);
	}
}
