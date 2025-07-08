package com.pinback.pinback_server.domain.article.domain.repository;

import static com.pinback.pinback_server.domain.article.domain.entity.QArticle.*;
import static com.pinback.pinback_server.domain.category.domain.entity.QCategory.*;
import static com.pinback.pinback_server.domain.user.domain.entity.QUser.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.dto.ArticlesWithUnreadCount;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable) {
		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(article.user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(article.user.id.eq(userId));

		Long unReadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(article.user.id.eq(userId).and(article.isRead.isFalse()))
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne));
	}

	@Override
	public ArticlesWithUnreadCount findAllByCategory(UUID userId, long categoryId, Pageable pageable) {

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.category, category).fetchJoin()
			.where(article.category.id.eq(categoryId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(article.category.id.eq(categoryId));

		Long unReadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(article.category.id.eq(categoryId).and(article.isRead.isFalse()))
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne));
	}
}
