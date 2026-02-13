package com.pinback.api.article.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.article.dto.request.ArticleCreateRequest;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponseV3;
import com.pinback.application.article.dto.response.GetAllArticlesResponseV3;
import com.pinback.application.article.dto.response.TodayRemindCountResponse;
import com.pinback.application.article.dto.response.TodayRemindResponseV3;
import com.pinback.application.article.port.in.CreateArticlePort;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.article.service.ArticleUpdateService;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v3/articles")
@RequiredArgsConstructor
@Tag(name = "ArticleV3", description = "아티클 관리 API V3")
public class ArticleControllerV3 {
	private final CreateArticlePort createArticlePort;
	private final GetArticlePort getArticlePort;
	private final ArticleUpdateService articleMetadataUpdateService;

	@Operation(summary = "아티클 생성v3", description = "url에서 썸네일과 제목을 추출하여 새로운 아티클을 생성합니다")
	@PostMapping
	public ResponseDto<Void> createArticle(
		@Parameter(hidden = true) @CurrentUser User user,
		@Valid @RequestBody ArticleCreateRequest request
	) {
		createArticlePort.createArticleV3(user, request.toCommand());
		return ResponseDto.ok();
	}

	@Operation(summary = "아티클 상세 조회 V3", description = "아티클의 상세 정보를 조회합니다")
	@GetMapping("/{articleId}")
	public ResponseDto<ArticleDetailResponseV3> getArticleDetail(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "아티클 ID") @PathVariable Long articleId
	) {
		ArticleDetailResponseV3 response = getArticlePort.getArticleDetailWithMetadata(user, articleId);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "리마인드 아티클 조회 v3", description = "오늘 리마인드할 아티클을 읽음/안읽음 상태별로 조회합니다.")
	@GetMapping("/remind")
	public ResponseDto<TodayRemindResponseV3> getRemindArticlesV3(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "현재 시간", example = "2026-02-13T10:00:00") @RequestParam LocalDateTime now,
		@Parameter(description = "읽음 상태 (true: 읽음, false: 안읽음)", example = "true") @RequestParam(name = "read-status") boolean readStatus,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		TodayRemindResponseV3 response = getArticlePort.getRemindArticlesV3(user, now, readStatus, query);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "리마인드 아티클 읽음/안읽음 개수 조회 v3", description = "오늘 리마인드할 아티클의 읽음/안읽음 개수를 반환합니다.")
	@GetMapping("/remind/count")
	public ResponseDto<TodayRemindCountResponse> getRemindArticlesInfo(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "현재 시간", example = "2026-02-13T10:00:00") @RequestParam LocalDateTime now
	) {
		TodayRemindCountResponse response = getArticlePort.getRemindArticlesInfo(user, now);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "나의 북마크 전체 아티클 조회 V3", description = "사용자의 모든 아티클을 페이징으로 조회합니다.")
	@GetMapping
	public ResponseDto<GetAllArticlesResponseV3> getAllArticles(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "읽음 상태 (생략: 읽음, false: 안읽음)", example = "false") @RequestParam(name = "read-status", required = false) Boolean readStatus,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		GetAllArticlesResponseV3 response = getArticlePort.getAllArticlesV3(user, readStatus, query);
		return ResponseDto.ok(response);
	}

	// 기존 아티클 메타데이터 처리 후 삭제 예정
	@PostMapping("/metadata")
	public ResponseDto<Void> migrateMetadata() {
		articleMetadataUpdateService.migrateMissingMetadata();
		return ResponseDto.ok();
	}
}
