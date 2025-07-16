package com.pinback.pinback_server.domain.article.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleDeleteService {
	private final ArticleRepository articleRepository;

	public void delete(Article article) {
		articleRepository.delete(article);
	}

	public void deleteByCategory(User user, Category category) {
		articleRepository.deleteByUserAndCategory(user, category);
	}

}
