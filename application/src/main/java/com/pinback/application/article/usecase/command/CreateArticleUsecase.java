package com.pinback.application.article.usecase.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.command.ArticleCreateCommand;
import com.pinback.application.article.dto.response.ArticleMetadataResponse;
import com.pinback.application.article.port.in.CreateArticlePort;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.article.port.out.ArticleSaveServicePort;
import com.pinback.application.category.port.in.GetCategoryPort;
import com.pinback.application.common.exception.ArticleAlreadyExistException;
import com.pinback.application.common.exception.MemoLengthLimitException;
import com.pinback.application.notification.port.in.GetPushSubscriptionPort;
import com.pinback.application.notification.port.in.ScheduleArticleReminderPort;
import com.pinback.application.test.port.out.ArticleMetadataPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.util.TextUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateArticleUsecase implements CreateArticlePort {

	private static final long MEMO_LIMIT_LENGTH = 500;

	private final ArticleGetServicePort articleGetService;
	private final ArticleSaveServicePort articleSaveService;

	private final GetCategoryPort getCategoryPort;

	private final GetPushSubscriptionPort getPushSubscription;
	private final ScheduleArticleReminderPort scheduleArticleReminder;

	private final ArticleMetadataPort articleMetadataPort;

	@Override
	public void createArticle(User user, ArticleCreateCommand command) {
		validateArticleCreation(user, command);

		Category category = getCategoryPort.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category, command.remindTime());
		Article savedArticle = articleSaveService.save(article);

		scheduleReminderIfNeeded(savedArticle, user, command.remindTime());
	}

	@Override
	public void createArticleV3(User user, ArticleCreateCommand command) {
		// 1. url 중복 검증
		validateArticleCreation(user, command);

		// 2. 메타데이터 가져오기
		ArticleMetadataResponse metadata = articleMetadataPort.extractMetadata(command.url());

		// 3.0 로그로 찍어서 확인해보기
		log.info("title: {}, thumbnail: {}", metadata.title(), metadata.thumbnailUrl());

		// 3. 아티클 저장
		Category category = getCategoryPort.getCategoryAndUser(command.categoryId(), user);
		// s3에 url 있는지 확인 후 있으면 가져오고, 없으면 저장해서 가져오기
		Article article = Article.createWithMetaData(command.url(), command.memo(), user, category,
			command.remindTime(), metadata.title(), metadata.thumbnailUrl());
		Article savedArticle = articleSaveService.save(article);
		scheduleReminderIfNeeded(savedArticle, user, command.remindTime());
	}

	private void validateArticleCreation(User user, ArticleCreateCommand command) {
		if (articleGetService.checkExistsByUserAndUrl(user, command.url())) {
			throw new ArticleAlreadyExistException();
		}

		if (TextUtil.countGraphemeClusters(command.memo()) >= MEMO_LIMIT_LENGTH) {
			throw new MemoLengthLimitException();
		}
	}

	private void scheduleReminderIfNeeded(Article article, User user, LocalDateTime remindTime) {
		if (remindTime != null && !remindTime.isBefore(LocalDateTime.now())) {
			PushSubscription subscriptionInfo = getPushSubscription.findPushSubscription(user);
			scheduleArticleReminder.schedule(article, user, subscriptionInfo.getToken());
		}
	}
}
