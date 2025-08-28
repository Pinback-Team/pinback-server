package com.pinback.application.notification.usecase.command;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.in.ScheduleArticleReminderPort;
import com.pinback.application.notification.port.out.NotificationServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleArticleReminderUsecase implements ScheduleArticleReminderPort {

	private final NotificationServicePort notificationService;

	@Override
	public void schedule(Article article, User user, String fcmToken) {
		notificationService.scheduleArticleReminder(article, user, fcmToken);
	}

	@Override
	public void cancel(long articleId, UUID userId) {
		notificationService.cancelArticleReminder(articleId, userId);
	}
}
