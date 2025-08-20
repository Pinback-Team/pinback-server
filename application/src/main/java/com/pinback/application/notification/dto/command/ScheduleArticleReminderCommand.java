package com.pinback.application.notification.dto.command;

import java.util.UUID;

public record ScheduleArticleReminderCommand(
	long articleId,
	UUID userId,
	String fcmToken
) {
}