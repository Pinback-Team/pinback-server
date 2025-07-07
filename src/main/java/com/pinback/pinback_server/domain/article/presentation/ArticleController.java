package com.pinback.pinback_server.domain.article.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.article.application.ArticleManagementUsecase;
import com.pinback.pinback_server.domain.article.presentation.dto.request.ArticleCreateRequest;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
	private final ArticleManagementUsecase articleManagementUsecase;

	@PostMapping
	public ResponseDto<Void> createArticle(@CurrentUser User user, @Valid @RequestBody ArticleCreateRequest request) {
		articleManagementUsecase.createArticle(user, request.toCommand());
		return ResponseDto.created();
	}
}
