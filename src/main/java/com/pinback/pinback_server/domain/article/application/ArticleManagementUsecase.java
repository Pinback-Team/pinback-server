package com.pinback.pinback_server.domain.article.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.application.command.ArticleUpdateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;
import com.pinback.pinback_server.domain.article.domain.service.ArticleDeleteService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleGetService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.article.exception.ArticleAlreadyExistException;
import com.pinback.pinback_server.domain.article.exception.ArticleNotOwnedException;
import com.pinback.pinback_server.domain.article.exception.MemoLengthLimitException;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleAllResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticlesResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ReadArticleResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.RemindArticleResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.RemindArticles;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;
import com.pinback.pinback_server.domain.notification.domain.service.PushSubscriptionGetService;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.util.TextUtil;
import com.pinback.pinback_server.infra.redis.AcornService;
import com.pinback.pinback_server.infra.redis.dto.response.AcornCollectResponse;
import com.pinback.pinback_server.infra.redis.service.RedisNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleManagementUsecase {

	private static final long MEMO_LIMIT_LENGTH = 1000;

	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;
	private final ArticleGetService articleGetService;
	private final ArticleDeleteService articleDeleteService;
	private final AcornService acornService;
	private final RedisNotificationService redisNotificationService;
	private final PushSubscriptionGetService pushSubscriptionGetService;

	@Transactional
	public void createArticle(User user, ArticleCreateCommand command) {
		if (articleGetService.checkExistsByUserAndUrl(user, command.url())) {
			throw new ArticleAlreadyExistException();
		}

		if (TextUtil.countGraphemeClusters(command.memo()) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}
		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category, command.remindTime());
		Article savedArticle = articleSaveService.save(article);

		if (command.remindTime() != null && !command.remindTime().isBefore(LocalDateTime.now())) {
			PushSubscription subscriptionInfo = pushSubscriptionGetService.find(user);
			redisNotificationService.scheduleArticleReminder(savedArticle, user, subscriptionInfo.getToken());
		}

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
		LocalDateTime remindDate = now.plusDays(1L);
		LocalDateTime remindDateTime = LocalDateTime.of(remindDate.getYear(), remindDate.getMonth(),
			remindDate.getDayOfMonth(),
			user.getRemindDefault().getHour(), user.getRemindDefault().getMinute());

		Page<Article> articles = articleGetService.findTodayRemind(user.getId(), now,
			PageRequest.of(pageNumber, pageSize));

		List<RemindArticles> articlesResponses = articles.stream()
			.map(RemindArticles::from)
			.toList();

		return new RemindArticleResponse(
			articles.getTotalElements(),
			remindDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a HH시 mm분", Locale.KOREAN)),
			articlesResponses
		);
	}

	@Transactional
	public ReadArticleResponse updateArticleStatus(User user, long articleId) {
		Article article = articleGetService.findByUserAndId(user, articleId);

		// acornService.resetAcornsForTest(user.getId()); //테스트용 redis 키 삭제 메서드
		int finalAcornCount = acornService.getCurrentAcorns(user.getId());
		log.info("수집하기 전 도토리 수: {}", finalAcornCount);
		boolean acornCollected = false; // 해당 요청으로 도토리가 수집되었는지 여부

		if (!article.isRead()) {
			article.toRead();

			AcornCollectResponse response = acornService.tryCollectAcorns(user);
			finalAcornCount = response.finalAcornCount();
			acornCollected = response.isCollected();
			return ReadArticleResponse.of(finalAcornCount, acornCollected);

		}
		return ReadArticleResponse.of(finalAcornCount, acornCollected);
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
		if (!(article.getUser().equals(user))) {
			throw new ArticleNotOwnedException();
		}
	}

	@Transactional
	public void update(User user, long articleId, ArticleUpdateCommand command) {
		if (TextUtil.countGraphemeClusters(command.memo()) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}

		boolean remindAtIsChanged = false;

		Article article = articleGetService.findById(articleId);
		if (!article.getRemindAt().equals(command.remindTime())) {
			remindAtIsChanged = true;
		}
		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		article.update(command.memo(), category, command.remindTime());

		PushSubscription subscriptionInfo = pushSubscriptionGetService.find(user);

		if (command.remindTime() != null && !command.remindTime().isBefore(LocalDateTime.now()) && remindAtIsChanged) {
			redisNotificationService.cancelArticleReminder(articleId, user.getId());
			redisNotificationService.scheduleArticleReminder(article, user, subscriptionInfo.getToken());
		}
	}

}
