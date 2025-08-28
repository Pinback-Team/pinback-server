package com.pinback.application.notification.usecase.command;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.in.ManageArticleReminderPort;
import com.pinback.application.notification.port.out.NotificationServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManageArticleReminderUsecase implements ManageArticleReminderPort {

	private final NotificationServicePort notificationService;

	@Override
	public void cancelArticleReminder(long articleId, UUID userId) {
		log.debug("Canceling article reminder for articleId: {}, userId: {}", articleId, userId);
		notificationService.cancelArticleReminder(articleId, userId);
	}

	@Override
	public void scheduleArticleReminder(Article article, User user, String token) {
		log.debug("Scheduling article reminder for articleId: {}, userId: {}", 
			article.getId(), user.getId());
		notificationService.scheduleArticleReminder(article, user, token);
	}
}
