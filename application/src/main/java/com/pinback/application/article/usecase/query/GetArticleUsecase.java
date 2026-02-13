package com.pinback.application.article.usecase.query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.ArticlesWithCountDto;
import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticleCountDtoV3;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDtoV2;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticleDetailResponseV3;
import com.pinback.application.article.dto.response.ArticleResponse;
import com.pinback.application.article.dto.response.ArticleResponseV3;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.GetAllArticlesResponse;
import com.pinback.application.article.dto.response.GetAllArticlesResponseV3;
import com.pinback.application.article.dto.response.RemindArticleResponse;
import com.pinback.application.article.dto.response.RemindArticleResponseV2;
import com.pinback.application.article.dto.response.RemindArticleResponseV3;
import com.pinback.application.article.dto.response.TodayRemindCountResponse;
import com.pinback.application.article.dto.response.TodayRemindResponse;
import com.pinback.application.article.dto.response.TodayRemindResponseV2;
import com.pinback.application.article.dto.response.TodayRemindResponseV3;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.common.exception.InvalidReadStatusException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetArticleUsecase implements GetArticlePort {

	private final ArticleGetServicePort articleGetServicePort;
	private final GetCategoryPort getCategoryPort;

	@Override
	public ArticleDetailResponse getArticleDetail(long articleId) {
		Article article = articleGetServicePort.findById(articleId);
		return ArticleDetailResponse.from(article);
	}

	@Override
	public ArticleDetailResponse checkArticleExists(User user, String url) {
		Optional<Article> article = articleGetServicePort.findByUrlAndUser(user, url);
		return article.map(ArticleDetailResponse::from).orElse(null);
	}

	@Override
	public GetAllArticlesResponse getAllArticles(User user, PageQuery query) {
		ArticlesWithUnreadCountDto result = articleGetServicePort.findAll(
			user, PageRequest.of(query.pageNumber(), query.pageSize()));

		List<ArticleResponse> articleResponses = result.article().stream()
			.map(ArticleResponse::from)
			.toList();

		boolean isNewUser = user.isNewUser(LocalDateTime.now());

		return GetAllArticlesResponse.of(
			result.article().getTotalElements(),
			result.unReadCount(),
			isNewUser,
			articleResponses
		);
	}

	@Override
	public ArticlesPageResponse getAllArticlesByCategory(User user, long categoryId, Boolean isRead, PageQuery query) {
		Category category = getCategoryPort.getCategoryAndUser(categoryId, user);

		ArticlesWithUnreadCountDto result = articleGetServicePort.findAllByCategory(
			user, category, isRead, PageRequest.of(query.pageNumber(), query.pageSize()));

		List<ArticleResponse> articleResponses = result.article().stream()
			.map(ArticleResponse::from)
			.toList();

		return ArticlesPageResponse.of(
			result.totalCategoryArticleCount(),
			result.unReadCount(),
			articleResponses
		);
	}

	@Override
	public ArticlesPageResponse getUnreadArticles(User user, PageQuery query) {
		ArticlesWithUnreadCountDto result = articleGetServicePort.findUnreadArticles(
			user, PageRequest.of(query.pageNumber(), query.pageSize()));

		List<ArticleResponse> articleResponses = result.article().stream()
			.map(ArticleResponse::from)
			.toList();

		return ArticlesPageResponse.of(
			result.article().getTotalElements(),
			result.unReadCount(),
			articleResponses
		);
	}

	@Override
	public TodayRemindResponse getRemindArticles(User user, LocalDateTime now, boolean readStatus, PageQuery query) {
		LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59, 999999999);

		RemindArticlesWithCountDto result = articleGetServicePort.findTodayRemindWithCount(
			user, startOfDay, endOfDay, PageRequest.of(query.pageNumber(), query.pageSize()), readStatus);

		List<RemindArticleResponse> articleResponses = result.articles().stream()
			.map(RemindArticleResponse::from)
			.toList();

		return TodayRemindResponse.of(
			result.readCount(),
			result.unreadCount(),
			articleResponses
		);
	}

	@Override
	public TodayRemindResponseV2 getRemindArticlesV2(
		User user,
		LocalDateTime now,
		boolean readStatus,
		PageQuery query
	) {
		LocalDateTime endBound = now;
		LocalDateTime startBound = now.minusHours(24);

		RemindArticlesWithCountDtoV2 result = articleGetServicePort.findTodayRemindWithCountV2(
			user,
			startBound,
			endBound,
			PageRequest.of(query.pageNumber(), query.pageSize()),
			readStatus
		);

		List<RemindArticleResponseV2> articleResponses =
			result.articles() != null ?
				result.articles().stream()
					.map(RemindArticleResponseV2::from)
					.toList() :
				Collections.emptyList();

		return TodayRemindResponseV2.of(
			result.hasNext(),
			result.totalCount(),
			result.readCount(),
			result.unreadCount(),
			articleResponses
		);
	}

	@Override
	public ArticleDetailResponseV3 getArticleDetailWithMetadata(User user, long articleId) {
		Article article = articleGetServicePort.findByUserAndId(user, articleId);
		return ArticleDetailResponseV3.from(article);
	}

	@Override
	public TodayRemindResponseV3 getRemindArticlesV3(
		User user,
		LocalDateTime now,
		boolean readStatus,
		PageQuery query
	) {
		LocalDateTime endBound = now;
		LocalDateTime startBound = now.minusHours(24);

		RemindArticlesWithCountDtoV2 result = articleGetServicePort.findTodayRemindWithCountV2(
			user,
			startBound,
			endBound,
			PageRequest.of(query.pageNumber(), query.pageSize()),
			readStatus
		);

		List<RemindArticleResponseV3> articleResponses =
			result.articles() != null ?
				result.articles().stream()
					.map(RemindArticleResponseV3::from)
					.toList() :
				Collections.emptyList();

		return TodayRemindResponseV3.of(
			result.hasNext(),
			result.totalCount(),
			result.readCount(),
			result.unreadCount(),
			articleResponses
		);
	}

	@Override
	public TodayRemindCountResponse getRemindArticlesInfo(User user, LocalDateTime now) {
		LocalDateTime endBound = now;
		LocalDateTime startBound = now.minusHours(24);

		RemindArticleCountDtoV3 result = articleGetServicePort.findTodayRemindCountV3(
			user,
			startBound,
			endBound
		);

		return TodayRemindCountResponse.of(
			result.totalCount(),
			result.readCount(),
			result.unreadCount()
		);
	}

	@Override
	public GetAllArticlesResponseV3 getAllArticlesV3(User user, Boolean readStatus, PageQuery query) {
		if (readStatus != null && readStatus) {
			throw new InvalidReadStatusException();

		}

		ArticlesWithCountDto result = articleGetServicePort.findAllByReadStatus(
			user, readStatus, PageRequest.of(query.pageNumber(), query.pageSize()));

		List<ArticleResponseV3> articleResponses = result.article().stream()
			.map(ArticleResponseV3::from)
			.toList();

		return GetAllArticlesResponseV3.of(
			result.totalCount(),
			result.unreadCount(),
			articleResponses
		);
	}

	private LocalDateTime getRemindDateTime(LocalDateTime now, LocalTime remindDefault) {
		return LocalDateTime.of(
			now.getYear(),
			now.getMonth(),
			now.getDayOfMonth(),
			remindDefault.getHour(),
			remindDefault.getMinute()
		);
	}
}
