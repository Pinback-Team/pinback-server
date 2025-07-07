package com.pinback.pinback_server.domain.article.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.article.exception.ArticleNotFoundException;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleGetService {
	private final ArticleRepository articleRepository;

	public boolean checkExistsByUserAndUrl(User user, String url) {
		return articleRepository.existsByUserAndUrl(user, url);
	}

	public Article findById(long articleId) {
		return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
	}
}
