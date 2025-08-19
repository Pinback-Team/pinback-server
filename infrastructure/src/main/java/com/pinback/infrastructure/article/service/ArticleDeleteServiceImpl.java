package com.pinback.infrastructure.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.service.ArticleDeleteService;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleDeleteServiceImpl implements ArticleDeleteService {

	private final ArticleRepository articleRepository;

	@Override
	public void deleteByCategory(User user, long categoryId) {
		articleRepository.deleteArticlesByUserIdAndCategoryId(user.getId(), categoryId);
	}

	@Override
	public void delete(Article article) {
		articleRepository.delete(article);
	}
}
