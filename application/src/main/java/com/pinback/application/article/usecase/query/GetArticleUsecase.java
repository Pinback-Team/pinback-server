package com.pinback.application.article.usecase.query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticleResponse;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.GetAllArticlesResponse;
import com.pinback.application.article.dto.response.RemindArticleResponse;
import com.pinback.application.article.dto.response.TodayRemindResponse;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
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
	public ArticlesPageResponse getAllArticlesByCategory(User user, long categoryId, boolean isRead, PageQuery query) {
		Category category = getCategoryPort.getCategoryAndUser(categoryId, user);

		ArticlesWithUnreadCountDto result = articleGetServicePort.findAllByCategory(
			user, category, isRead, PageRequest.of(query.pageNumber(), query.pageSize()));

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
		LocalDateTime remindDateTime = getRemindDateTime(now, user.getRemindDefault());

		RemindArticlesWithCountDto result = articleGetServicePort.findTodayRemindWithCount(
			user, remindDateTime, PageRequest.of(query.pageNumber(), query.pageSize()), readStatus);

		List<RemindArticleResponse> articleResponses = result.articles().stream()
			.map(RemindArticleResponse::from)
			.toList();

		return TodayRemindResponse.of(
			result.readCount(),
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
