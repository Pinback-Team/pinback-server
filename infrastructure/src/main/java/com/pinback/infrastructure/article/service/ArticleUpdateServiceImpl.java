package com.pinback.infrastructure.article.service;

import org.springframework.stereotype.Service;

import com.pinback.application.article.service.ArticleUpdateService;
import com.pinback.application.common.exception.ArticleNotFoundException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleUpdateServiceImpl implements ArticleUpdateService {

	private final ArticleRepository articleRepository;

	@Override
	public Article findByUserAndId(User user, long articleId) {
		return articleRepository.findArticleByUserAndId(user, articleId).orElseThrow(ArticleNotFoundException::new);
	}
}
