package com.pinback.pinback_server.domain.article.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleSaveService {
	private final ArticleRepository articleRepository;

	public void save(Article article) {
		articleRepository.save(article);
	}
}
