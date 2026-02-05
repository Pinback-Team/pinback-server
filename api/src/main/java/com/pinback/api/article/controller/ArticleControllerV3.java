package com.pinback.api.article.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.article.dto.request.ArticleCreateRequest;
import com.pinback.application.article.port.in.CreateArticlePort;
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

	@PostMapping("/metadata")
	public ResponseDto<Void> migrateMetadata() {
		articleMetadataUpdateService.migrateMissingMetadata();
		return ResponseDto.ok();
	}
}
