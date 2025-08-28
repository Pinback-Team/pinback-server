package com.pinback.infrastructure.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.port.out.ArticleSaveServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.infrastructure.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleSaveService implements ArticleSaveServicePort {

	private final ArticleRepository articleRepository;

	@Override
	public Article save(Article article) {
		return articleRepository.save(article);
	}
}
