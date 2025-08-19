package com.pinback.application.article.dto.command;

import java.time.LocalDateTime;

public record ArticleCreateCommand(
	String url,
	long categoryId,
	String memo,
	LocalDateTime remindTime
) {
}