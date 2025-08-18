package com.pinback.domain.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleDeleteService {

	private final ArticleRepository articleRepository;

	public void deleteById(Long articleId) {
		articleRepository.deleteById(articleId);
	}
}
