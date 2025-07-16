package com.pinback.pinback_server.domain.article.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleDeleteService {
	private final ArticleRepository articleRepository;

	public void delete(Article article) {
		articleRepository.delete(article);
	}

	public void deleteByCategory(UUID userId, long categoryId) {
		articleRepository.deleteByUserAndCategory(userId, categoryId);
	}

}
