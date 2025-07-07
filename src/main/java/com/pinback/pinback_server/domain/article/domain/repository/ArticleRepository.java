package com.pinback.pinback_server.domain.article.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.user.domain.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	boolean existsByUserAndUrl(User user, String url);
}
