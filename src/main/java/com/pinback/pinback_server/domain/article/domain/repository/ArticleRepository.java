package com.pinback.pinback_server.domain.article.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.user.domain.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

	boolean existsByUserAndUrl(User user, String url);

	@Query("SELECT a FROM Article a WHERE a.user = :user ORDER BY a.createdAt DESC LIMIT 1")
	Optional<Article> findRecentArticleByUser(@Param("user") User user);

	Optional<Article> findArticleByUserAndUrl(User user, String url);

	Optional<Article> findArticleByUserAndId(User user, Long id);
}
