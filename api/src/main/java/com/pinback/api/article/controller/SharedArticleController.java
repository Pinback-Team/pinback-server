package com.pinback.api.article.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.article.dto.response.SharedArticleDetailResponse;
import com.pinback.application.article.dto.response.SharedArticlePageResponse;
import com.pinback.application.article.port.in.GetSharedArticlePort;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.annotation.CurrentUser;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v3/articles/shared/job")
@RequiredArgsConstructor
@Tag(name = "Shared Article", description = "관심 직무 핀 관리 API")
public class SharedArticleController {
	private final GetSharedArticlePort getSharedArticlePort;

	@Operation(summary = "관심 직무 아티클 전체 조회", description = "관심 직무 핀의 아티클 목록을 조회합니다")
	@GetMapping
	public ResponseDto<SharedArticlePageResponse> getAllSharedArticle(
		@Parameter(hidden = true) @CurrentUser User user
	) {
		SharedArticlePageResponse response = getSharedArticlePort.getAllSharedArticle(user);
		return ResponseDto.ok(response);
	}

	@Operation(summary = "관심 직무 아티클 상세 조회", description = "관심 직무 아티클의 상세 정보를 조회합니다")
	@GetMapping("/{articleId}")
	public ResponseDto<SharedArticleDetailResponse> getSharedArticleDetail(
		@Parameter(hidden = true) @CurrentUser User user,
		@Parameter(description = "아티클 ID") @PathVariable Long articleId
	) {
		SharedArticleDetailResponse response = getSharedArticlePort.getSharedArticleDetail(user, articleId);
		return ResponseDto.ok(response);
	}

}
