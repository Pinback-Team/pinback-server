package com.pinback.pinback_server.domain.article.presentation;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.pinback_server.domain.article.application.ArticleManagementUsecase;
import com.pinback.pinback_server.domain.article.presentation.dto.request.ArticleCreateRequest;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleAllResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.RemindArticleResponse;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.common.annotation.CurrentUser;
import com.pinback.pinback_server.global.common.dto.ResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
	private final ArticleManagementUsecase articleManagementUsecase;

	@PostMapping
	public ResponseDto<Void> createArticle(@CurrentUser User user, @Valid @RequestBody ArticleCreateRequest request) {
		articleManagementUsecase.createArticle(user, request.toCommand());
		return ResponseDto.created();
	}

	@GetMapping("/details/{articleId}")
	public ResponseDto<ArticleDetailResponse> getArticleDetails(@PathVariable Long articleId) {
		ArticleDetailResponse response = articleManagementUsecase.getArticleDetail(articleId);
		return ResponseDto.ok(response);
	}

	@GetMapping
	public ResponseDto<ArticleAllResponse> getAll(@CurrentUser User user, @RequestParam int pageNumber,
		@RequestParam int pageSize) {

		ArticleAllResponse response = articleManagementUsecase.getAllArticles(user, pageNumber, pageSize);
		return ResponseDto.ok(response);
	}

	@GetMapping("/{categoryId}")
	public ResponseDto<ArticleAllResponse> getAllByCategory(@CurrentUser User user, @RequestParam Long categoryId,
		@RequestParam int pageNumber,
		@RequestParam int pageSize) {

		ArticleAllResponse response = articleManagementUsecase.getAllArticlesByCategory(user, categoryId, pageNumber,
			pageSize);
		return ResponseDto.ok(response);
	}

	@GetMapping("/remind")
	public ResponseDto<RemindArticleResponse> getAllRemindArticles(@CurrentUser User user,
		@RequestParam LocalDateTime now,
		@RequestParam int pageNumber,
		@RequestParam int pageSize) {

		RemindArticleResponse response = articleManagementUsecase.getRemindArticles(user, now, pageNumber,
			pageSize);
		return ResponseDto.ok(response);
	}

}
