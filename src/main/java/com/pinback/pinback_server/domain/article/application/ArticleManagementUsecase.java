package com.pinback.pinback_server.domain.article.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.service.ArticleGetService;
import com.pinback.pinback_server.domain.article.domain.service.ArticleSaveService;
import com.pinback.pinback_server.domain.article.exception.ArticleAlreadyExistException;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleAllResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticlesResponse;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.service.CategoryGetService;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleManagementUsecase {

	private final CategoryGetService categoryGetService;
	private final ArticleSaveService articleSaveService;
	private final ArticleGetService articleGetService;

	//TODO: 리마인드 로직 추가 필요
	@Transactional
	public void createArticle(User user, ArticleCreateCommand command) {
		if (articleGetService.checkExistsByUserAndUrl(user, command.url())) {
			throw new ArticleAlreadyExistException();
		}
		Category category = categoryGetService.getCategoryAndUser(command.categoryId(), user);
		Article article = Article.create(command.url(), command.memo(), user, category, command.remindTime());
		articleSaveService.save(article);
	}

	public ArticleDetailResponse getArticleDetail(long articleId) {
		Article article = articleGetService.findById(articleId);
		return ArticleDetailResponse.from(article);
	}

	public ArticleAllResponse getAllArticles(User user, int pageNumber, int pageSize) {
		Page<Article> articles = articleGetService.findAll(user.getId(), PageRequest.of(pageNumber, pageSize));

		List<ArticlesResponse> articlesResponses = articles.stream()
			.map(ArticlesResponse::from)
			.toList();

		return ArticleAllResponse.of(
			articles.getTotalElements(),
			articlesResponses
		);
	}
}
