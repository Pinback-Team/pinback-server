package com.pinback.api.article.dto.request;

import java.time.LocalDateTime;

import com.pinback.application.article.dto.command.ArticleUpdateCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "아티클 수정 요청")
public record ArticleUpdateRequest(
	@Schema(description = "카테고리 ID", example = "1")
	@NotNull(message = "카테고리 ID는 필수입니다")
	@Positive(message = "카테고리 ID는 양수여야 합니다")
	Long categoryId,
	
	@Schema(description = "메모", example = "수정된 메모입니다")
	@Size(max = 500, message = "메모는 500자 이하로 입력해주세요")
	String memo,
	
	@Schema(description = "리마인더 시간", example = "2025-12-31T23:59:00")
	LocalDateTime remindTime
) {
	public ArticleUpdateCommand toCommand() {
		return new ArticleUpdateCommand(categoryId, memo, remindTime);
	}
}