package com.pinback.pinback_server.domain.article.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;
import com.pinback.pinback_server.domain.article.domain.service.ArticleDeleteService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleGetService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.article.exception.ArticleAlreadyExistException;
import com.pinback.pinback_server.domain.article.exception.ArticleNotOwnedException;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleAllResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticlesResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.RemindArticleResponse;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleManagementUsecase {

	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;
	private final ArticleGetService articleGetService;
	private final ArticleDeleteService articleDeleteService;

	//TODO: 리마인드 로직 추가 필요
	@Transactional
	public void createArticle(User user, ArticleCreateCommand command) {
		if (articleGetService.checkExistsByUserAndUrl(user, command.url())) {
			throw new ArticleAlreadyExistException();
		}
		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category, command.remindTime());
		articleSaveService.save(article);
	}

	public ArticleDetailResponse getArticleDetail(long articleId) {
		Article article = articleGetService.findById(articleId);
		return ArticleDetailResponse.from(article);
	}

	public ArticleAllResponse getAllArticles(User user, int pageNumber, int pageSize) {
		ArticlesWithUnreadCount projection = articleGetService.findAll(user.getId(),
			PageRequest.of(pageNumber, pageSize));

		List<ArticlesResponse> articlesResponses = projection.getArticle().stream()
			.map(ArticlesResponse::from)
			.toList();

		return ArticleAllResponse.of(
			projection.getArticle().getTotalElements(),
			projection.getUnReadCount(),
			articlesResponses
		);
	}

	public ArticleAllResponse getAllArticlesByCategory(User user, long categoryId, int pageNumber, int pageSize) {

		Category category = categoryGetService.getCategoryAndUser(categoryId, user);

		ArticlesWithUnreadCount projection = articleGetService.findAllByCategory(user.getId(), category,
			PageRequest.of(pageNumber, pageSize));

		List<ArticlesResponse> articlesResponses = projection.getArticle().stream()
			.map(ArticlesResponse::from)
			.toList();

		return ArticleAllResponse.of(
			projection.getArticle().getTotalElements(),
			projection.getUnReadCount(),
			articlesResponses
		);
	}

	public RemindArticleResponse getRemindArticles(User user, LocalDateTime now, int pageNumber, int pageSize) {
		LocalDateTime nextDate = now.plusDays(1L);
		LocalDateTime nextRemindTime = LocalDateTime.of(nextDate.getYear(), nextDate.getMonth(),
			nextDate.getDayOfMonth(),
			user.getRemindDefault().getHour(), user.getRemindDefault().getMinute());

		Page<Article> articles = articleGetService.findTodayRemind(user.getId(), now,
			PageRequest.of(pageNumber, pageSize));

		List<ArticlesResponse> articlesResponses = articles.stream()
			.map(ArticlesResponse::from)
			.toList();

		return new RemindArticleResponse(
			articles.getTotalElements(),
			nextRemindTime,
			articlesResponses
		);
	}

	public ArticleDetailResponse checkArticleExists(User user, String url) {
		Optional<Article> article = articleGetService.findByUrlAndUser(user, url);

		return article.map(ArticleDetailResponse::from).orElse(null);
	}

	@Transactional
	public void delete(User user, long articleId) {
		Article article = articleGetService.findById(articleId);
		checkOwner(article, user);
		articleDeleteService.delete(article);
	}

	public void checkOwner(Article article, User user) {
		if (!(article.getUser() == user)) {
			throw new ArticleNotOwnedException();
		}
	}

}
