package com.pinback.application.article.usecase.query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticleResponse;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.RemindArticlesResponse;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.application.article.service.ArticleGetServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetArticleUsecase implements GetArticlePort {

	private final ArticleGetServicePort articleGetServicePort;
	private final CategoryGetServicePort categoryGetServicePort;

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
	public ArticlesPageResponse getAllArticles(User user, PageQuery query) {
		ArticlesWithUnreadCountDto result = articleGetServicePort.findAll(
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
	public ArticlesPageResponse getAllArticlesByCategory(User user, long categoryId, PageQuery query) {
		Category category = categoryGetServicePort.getCategoryAndUser(categoryId, user);

		ArticlesWithUnreadCountDto result = articleGetServicePort.findAllByCategory(
			user, category, PageRequest.of(query.pageNumber(), query.pageSize()));

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
	public RemindArticlesResponse getRemindArticles(User user, LocalDateTime now, PageQuery query) {
		LocalDateTime remindDateTime = getRemindDateTime(now, user.getRemindDefault());

		Page<Article> articles = articleGetServicePort.findTodayRemind(
			user, remindDateTime, PageRequest.of(query.pageNumber(), query.pageSize()));

		List<ArticleResponse> articleResponses = articles.stream()
			.map(ArticleResponse::from)
			.toList();

		return RemindArticlesResponse.of(
			articles.getTotalElements(),
			remindDateTime.plusDays(1),
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
