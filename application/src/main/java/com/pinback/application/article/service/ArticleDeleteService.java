package com.pinback.application.article.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.article.entity.Article;
import com.pinback.infrastructure.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleDeleteService {

	private final ArticleRepository articleRepository;

	public void delete(Article article) {
		articleRepository.delete(article);
	}

	public void deleteAllByCategory(UUID userId, long categoryId) {
		articleRepository.deleteArticlesByUserIdAndCategoryId(userId, categoryId);
	}
}
