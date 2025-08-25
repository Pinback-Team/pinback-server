package com.pinback.pinback_server.domain.article.presentation.dto.request;

import java.time.LocalDateTime;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArticleCreateRequest(
	@NotEmpty(message = "url을 비어있을 수 없습니다.")
	String url,

	@NotNull(message = "카테고리 ID는 비어있을 수 없습니다.")
	Long categoryId,

	String memo,

	LocalDateTime remindTime
) {
	public ArticleCreateCommand toCommand() {
		return new ArticleCreateCommand(
			url,
			categoryId,
			memo,
			remindTime
		);
	}
}
