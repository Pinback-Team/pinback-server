package com.pinback.infrastructure.article.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pinback.application.article.dto.ArticleCountInfoDtoV3;
import com.pinback.application.article.dto.ArticlesWithCountDto;
import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDtoV2;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.common.exception.ArticleNotFoundException;
import com.pinback.application.common.exception.ArticleNotOwnedException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.article.repository.ArticleRepository;
import com.pinback.infrastructure.article.repository.dto.ArticleCountInfoV3;
import com.pinback.infrastructure.article.repository.dto.ArticleWithCountV3;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCountV2;

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
	public ArticlesWithUnreadCountDto findAllByCategory(User user, Category category, Boolean isRead,
		PageRequest pageRequest) {
		ArticlesWithUnreadCount infraResult = articleRepository.findAllByCategory(user.getId(), category.getId(),
			isRead,
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
		return articleRepository.findArticleByUserAndId(user, articleId).orElseThrow(ArticleNotOwnedException::new);
	}

	@Override
	public Page<Article> findTodayRemind(User user, LocalDateTime remindDateTime, Pageable pageable, Boolean isRead) {
		return articleRepository.findTodayRemind(user.getId(), pageable, remindDateTime, remindDateTime.plusDays(1),
			isRead);
	}

	@Override
	public RemindArticlesWithCountDto findTodayRemindWithCount(User user, LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Pageable pageable, Boolean isRead) {
		RemindArticlesWithCount infraResult = articleRepository.findTodayRemindWithCount(user.getId(), pageable,
			startDateTime, endDateTime, isRead);
		return new RemindArticlesWithCountDto(
			infraResult.readCount(),
			infraResult.unreadCount(),
			infraResult.articles()
		);
	}

	@Override
	public RemindArticlesWithCountDtoV2 findTodayRemindWithCountV2(
		User user,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		Pageable pageable,
		Boolean isReadAtferRemind
	) {
		RemindArticlesWithCountV2 infraResult = articleRepository.findTodayRemindWithCountV2(
			user.getId(),
			pageable,
			startDateTime,
			endDateTime,
			isReadAtferRemind
		);
		return new RemindArticlesWithCountDtoV2(
			infraResult.articles().hasNext(),
			infraResult.readCount(),
			infraResult.unreadCount(),
			infraResult.totalCount(),
			infraResult.articles()
		);
	}

	@Override
	public ArticleCountInfoDtoV3 findTodayRemindCountV3(User user, LocalDateTime startDateTime,
		LocalDateTime endDateTime) {
		ArticleCountInfoV3 infraResult = articleRepository.findTodayRemindCountV3(
			user.getId(),
			startDateTime,
			endDateTime
		);
		return new ArticleCountInfoDtoV3(
			infraResult.totalCount(),
			infraResult.readCount(),
			infraResult.unreadCount()

		);
	}

	@Override
	public ArticlesWithCountDto findAllByReadStatus(User user, Boolean readStatus, PageRequest pageRequest) {
		ArticleWithCountV3 infraResult = articleRepository.findAllByReadStatus(
			user.getId(),
			readStatus,
			pageRequest
		);
		return new ArticlesWithCountDto(
			infraResult.totalCount(),
			infraResult.unreadCount(),
			infraResult.article()
		);
	}

	@Override
	public ArticleCountInfoDtoV3 findAllCountV3(User user) {
		ArticleCountInfoV3 infraResult = articleRepository.findAllCountV3(user.getId());
		return new ArticleCountInfoDtoV3(
			infraResult.totalCount(),
			infraResult.readCount(),
			infraResult.unreadCount()

		);
	}

	@Override
	public ArticlesWithCountDto findAllByCategoryAndReadStatus(
		User user,
		Category category,
		Boolean readStatus,
		PageRequest pageRequest
	) {
		ArticleWithCountV3 infraResult = articleRepository.findAllByCategoryAndReadStatus(
			user.getId(),
			category.getId(),
			readStatus,
			pageRequest
		);
		return new ArticlesWithCountDto(
			infraResult.totalCount(),
			infraResult.unreadCount(),
			infraResult.article()
		);
	}

	@Override
	public ArticleCountInfoDtoV3 findAllCountByCategoryV3(User user, Category category) {
		ArticleCountInfoV3 infraResult = articleRepository.findAllCountByCategoryV3(user.getId(), category.getId());
		return new ArticleCountInfoDtoV3(
			infraResult.totalCount(),
			infraResult.readCount(),
			infraResult.unreadCount()
		);
	}

	private ArticlesWithUnreadCountDto convertToDto(ArticlesWithUnreadCount infraResult) {
		return new ArticlesWithUnreadCountDto(
			infraResult.unReadCount(),
			infraResult.article(),
			infraResult.totalCategoryArticleCount()
		);
	}
}
