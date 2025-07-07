package com.pinback.pinback_server.domain.article.application.command;

import java.time.LocalDateTime;

public record ArticleCreateCommand(
	String url,
	long categoryId,
	String memo,
	LocalDateTime remindTime
) {
}
