package com.pinback.infrastructure.article.repository;

import static com.pinback.domain.article.entity.QArticle.*;
import static com.pinback.domain.category.entity.QCategory.*;
import static com.pinback.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pinback.domain.article.entity.Article;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public ArticlesWithUnreadCount findAllCustom(UUID userId, Pageable pageable) {

		BooleanExpression conditions = article.user.id.eq(userId);

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		Long unReadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions.and(article.isRead.isFalse()))
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne));
	}

	@Override
	public ArticlesWithUnreadCount findAllByCategory(UUID userId, long categoryId, Pageable pageable) {

		BooleanExpression conditions = article.category.id.eq(categoryId);

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.category, category).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		Long unReadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions.and(article.isRead.isFalse()))
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne));
	}

	@Override
	public Page<Article> findTodayRemind(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt) {
		BooleanExpression conditions = article.user.id.eq(userId)
			.and(article.remindAt.goe(startAt).and(article.remindAt.loe(endAt)));

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
	}

	@Override
	public ArticlesWithUnreadCount findAllByIsReadFalse(UUID userId, Pageable pageable) {
		BooleanExpression conditions = article.user.id.eq(userId).and(article.isRead.isFalse());

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		Long unReadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions)
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne));
	}

	@Override
	public void deleteArticlesByUserIdAndCategoryId(UUID userId, long categoryId) {
		BooleanExpression conditions = article.user.id.eq(userId).and(article.category.id.eq(categoryId));

		queryFactory
			.delete(article)
			.where(conditions)
			.execute();
	}
}
