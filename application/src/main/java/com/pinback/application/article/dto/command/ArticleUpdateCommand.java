package com.pinback.application.article.dto.command;

import java.time.LocalDateTime;

public record ArticleUpdateCommand(
	Long categoryId,
	String memo,
	LocalDateTime now,
	LocalDateTime remindTime
) {
}
