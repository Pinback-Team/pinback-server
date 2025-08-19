package com.pinback.infrastructure.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

	boolean existsByUserAndUrl(User user, String url);

	Optional<Article> findTopByUserOrderByCreatedAtDesc(User user);

	Optional<Article> findArticleByUserAndUrl(User user, String url);

	Optional<Article> findArticleByUserAndId(User user, Long id);

}
