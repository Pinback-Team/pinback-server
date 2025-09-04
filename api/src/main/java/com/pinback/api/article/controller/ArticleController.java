package com.pinback.api.article.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.article.dto.request.ArticleCreateRequest;
import com.pinback.api.article.dto.request.ArticleUpdateRequest;
import com.pinback.application.article.dto.query.PageQuery;
import com.pinback.application.article.dto.response.ArticleDetailResponse;
import com.pinback.application.article.dto.response.ArticlesPageResponse;
import com.pinback.application.article.dto.response.ReadArticleResponse;
import com.pinback.application.article.dto.response.TodayRemindResponse;
import com.pinback.application.article.port.in.CreateArticlePort;
import com.pinback.application.article.port.in.DeleteArticlePort;
import com.pinback.application.article.port.in.GetArticlePort;
import com.pinback.application.article.port.in.UpdateArticlePort;
import com.pinback.application.article.port.in.UpdateArticleStatusPort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Article", description = "아티클 관리 API")
public class ArticleController {

	private final CreateArticlePort createArticlePort;
	private final GetArticlePort getArticlePort;
	private final UpdateArticlePort updateArticlePort;
	private final UpdateArticleStatusPort updateArticleStatusPort;
	private final DeleteArticlePort deleteArticlePort;

	@Operation(summary = "아티클 생성", description = "새로운 아티클을 생성합니다")
	@PostMapping
	public ResponseDto<Void> createArticle(
		@Parameter(hidden = true) @CurrentUser User user,
		@Valid @RequestBody ArticleCreateRequest request
	) {
		createArticlePort.createArticle(user, request.toCommand());
		return ResponseDto.ok();
	}

	@Operation(summary = "아티클 상세 조회", description = "아티클의 상세 정보를 조회합니다")
	@GetMapping("/{articleId}")
	public ResponseDto<ArticleDetailResponse> getArticleDetail(
		@Parameter(description = "아티클 ID") @PathVariable Long articleId
	) {
		ArticleDetailResponse response = getArticlePort.getArticleDetail(articleId);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "아티클 존재 여부 확인", description = "URL로 아티클 존재 여부를 확인합니다")
	@GetMapping
	public ResponseDto<ArticleDetailResponse> checkArticleExists(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "확인할 URL") @RequestParam String url
	) {
		ArticleDetailResponse response = getArticlePort.checkArticleExists(user, url);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "모든 아티클 조회", description = "사용자의 모든 아티클을 페이징으로 조회합니다")
	@GetMapping
	public ResponseDto<ArticlesPageResponse> getAllArticles(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		ArticlesPageResponse response = getArticlePort.getAllArticles(user, query);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "카테고리별 아티클 조회", description = "특정 카테고리의 아티클을 조회합니다")
	@GetMapping("/category/{categoryId}")
	public ResponseDto<ArticlesPageResponse> getAllArticlesByCategory(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		ArticlesPageResponse response = getArticlePort.getAllArticlesByCategory(user, categoryId, query);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "읽지 않은 아티클 조회", description = "읽지 않은 아티클만 조회합니다")
	@GetMapping("/unread")
	public ResponseDto<ArticlesPageResponse> getUnreadArticles(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		ArticlesPageResponse response = getArticlePort.getUnreadArticles(user, query);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "아티클 수정", description = "아티클 정보를 수정합니다")
	@PutMapping("/{articleId}")
	public ResponseDto<Void> updateArticle(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "아티클 ID") @PathVariable Long articleId,
		@Valid @RequestBody ArticleUpdateRequest request
	) {
		updateArticlePort.updateArticle(user, articleId, request.toCommand());
		return ResponseDto.ok();
	}

	@Operation(summary = "아티클 읽음 처리", description = "아티클을 읽음으로 처리하고 도토리를 수집합니다")
	@PutMapping("/{articleId}/read")
	public ResponseDto<ReadArticleResponse> updateArticleStatus(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "아티클 ID") @PathVariable Long articleId
	) {
		ReadArticleResponse response = updateArticleStatusPort.updateArticleStatus(user, articleId);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "리마인드 아티클 조회", description = "오늘 리마인드할 아티클을 읽음/안읽음 상태별로 조회합니다")
	@GetMapping("/remind")
	public ResponseDto<TodayRemindResponse> getRemindArticles(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "현재 시간", example = "2025-09-03T10:00:00") @RequestParam LocalDateTime now,
		@Parameter(description = "읽음 상태 (true: 읽음, false: 안읽음)", example = "true") @RequestParam(name = "read-status") boolean readStatus,
		@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기") @RequestParam(defaultValue = "8") int size
	) {
		PageQuery query = new PageQuery(page, size);
		TodayRemindResponse response = getArticlePort.getRemindArticles(user, now, readStatus, query);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "아티클 삭제", description = "아티클을 삭제합니다")
	@DeleteMapping("/{articleId}")
	public ResponseDto<Void> deleteArticle(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "아티클 ID") @PathVariable Long articleId
	) {
		deleteArticlePort.deleteArticle(user, articleId);
		return ResponseDto.ok();
	}
}
