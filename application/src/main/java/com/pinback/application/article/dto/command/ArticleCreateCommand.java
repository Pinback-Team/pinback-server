package com.pinback.application.article.dto.command;

import java.time.LocalDateTime;

public record ArticleCreateCommand(
	String url,
	Long categoryId,
	String memo,
	LocalDateTime remindTime
) {
}
