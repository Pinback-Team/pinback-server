package com.pinback.infrastructure.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

	boolean existsByUserAndUrl(User user, String url);

	Optional<Article> findArticleByUserAndUrl(User user, String url);

	Optional<Article> findArticleByUserAndId(User user, Long id);

	@Query("SELECT a FROM Article a WHERE a.user = :user ORDER BY a.createdAt DESC LIMIT 1")
	Optional<Article> findRecentArticleByUser(@Param("user") User user);

	void deleteAllByUser(User user);
}
