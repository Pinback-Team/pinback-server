package com.pinback.pinback_server.domain.article.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pinback.pinback_server.domain.article.domain.entity.Article;

public interface ArticleRepositoryCustom {
	Page<Article> findAllCustom(UUID userId, Pageable pageable);
}
