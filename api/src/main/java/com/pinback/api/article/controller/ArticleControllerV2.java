package com.pinback.api.article.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.TodayRemindResponseV2;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/articles")
@RequiredArgsConstructor
@Tag(name = "ArticleV2", description = "아티클 관리 API V2")
public class ArticleControllerV2 {
	private final GetArticlePort getArticlePort;

	@Operation(summary = "리마인드 아티클 조회 v2", description = "오늘 리마인드할 아티클을 읽음/안읽음 상태별로 조회합니다.")
	@GetMapping("/remind")
	public ResponseDto<TodayRemindResponseV2> getRemindArticlesV2(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "현재 시간", example = "2025-09-03T10:00:00") @RequestParam LocalDateTime now,
		@Parameter(description = "읽음 상태 (true: 읽음, false: 안읽음)", example = "true") @RequestParam(name = "read-status") boolean readStatus,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		TodayRemindResponseV2 response = getArticlePort.getRemindArticlesV2(user, now, readStatus, query);
		return ResponseDto.ok(response);
	}
}
