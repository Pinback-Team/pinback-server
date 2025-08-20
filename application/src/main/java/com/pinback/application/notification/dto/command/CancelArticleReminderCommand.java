package com.pinback.application.notification.dto.command;

import java.util.UUID;

public record CancelArticleReminderCommand(
	long articleId,
	UUID userId
) {
}