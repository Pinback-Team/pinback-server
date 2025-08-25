package com.pinback.pinback_server.domain.article.presentation.dto.request;

import java.time.LocalDateTime;

import com.pinback.pinback_server.domain.article.application.command.ArticleUpdateCommand;

import jakarta.validation.constraints.NotNull;

public record ArticleUpdateRequest(
	@NotNull(message = "카테고리 ID는 비어있을 수 없습니다.")
	Long categoryId,

	String memo,

	LocalDateTime remindTime
) {
	public ArticleUpdateCommand toCommand() {
		return new ArticleUpdateCommand(
			categoryId,
			memo,
			remindTime
		);
	}
}
