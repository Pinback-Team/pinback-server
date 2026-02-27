package com.pinback.infrastructure.article.repository;

import static com.pinback.domain.article.entity.QArticle.*;
import static com.pinback.domain.category.entity.QCategory.*;
import static com.pinback.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.common.enums.Job;
import com.pinback.infrastructure.article.repository.dto.ArticleCountInfoV3;
import com.pinback.infrastructure.article.repository.dto.ArticleInfoV3;
import com.pinback.infrastructure.article.repository.dto.ArticleWithCountV3;
import com.pinback.infrastructure.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCount;
import com.pinback.infrastructure.article.repository.dto.RemindArticlesWithCountV2;
import com.pinback.infrastructure.article.repository.dto.SharedArticle;
import com.pinback.infrastructure.article.repository.dto.SharedArticles;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne),
			null);
	}

	@Override
	public ArticlesWithUnreadCount findAllByCategory(UUID userId, long categoryId, Boolean isRead, Pageable pageable) {

		BooleanExpression conditions = article.user.id.eq(userId)
			.and(article.category.id.eq(categoryId));

		if (isRead != null) {
			conditions = conditions.and(article.isRead.eq(isRead));
		}

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
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
			.where(article.user.id.eq(userId)
				.and(article.category.id.eq(categoryId))
				.and(article.isRead.isFalse()))
			.fetchOne();

		Long totalCategoryArticleCount = queryFactory
			.select(article.count())
			.from(article)
			.where(article.user.id.eq(userId).and(article.category.id.eq(categoryId)))
			.fetchOne();

		return new ArticlesWithUnreadCount(unReadCount,
			new PageImpl<>(articles, pageable, countQuery.fetchOne()),
			totalCategoryArticleCount);
	}

	@Override
	public Page<Article> findTodayRemind(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt, Boolean isRead) {
		BooleanExpression conditions = article.user.id.eq(userId)
			.and(article.remindAt.goe(startAt).and(article.remindAt.loe(endAt)));

		if (isRead != null) {
			conditions = conditions.and(article.isRead.eq(isRead));
		}

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
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne),
			null);
	}

	@Override
	public RemindArticlesWithCount findTodayRemindWithCount(UUID userId, Pageable pageable, LocalDateTime startAt,
		LocalDateTime endAt, Boolean isRead) {
		BooleanExpression baseConditions = article.user.id.eq(userId)
			.and(article.remindAt.goe(startAt).and(article.remindAt.loe(endAt)));

		BooleanExpression conditions = baseConditions.and(article.isRead.eq(isRead));

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.remindAt.asc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		Long readCount = queryFactory
			.select(article.count())
			.from(article)
			.where(baseConditions.and(article.isRead.isTrue()))
			.fetchOne();

		Long unreadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(baseConditions.and(article.isRead.isFalse()))
			.fetchOne();

		return new RemindArticlesWithCount(
			readCount,
			unreadCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne)
		);
	}

	@Override
	public void deleteArticlesByUserIdAndCategoryId(UUID userId, long categoryId) {
		BooleanExpression conditions = article.user.id.eq(userId).and(article.category.id.eq(categoryId));

		queryFactory
			.delete(article)
			.where(conditions)
			.execute();
	}

	@Override
	public RemindArticlesWithCountV2 findTodayRemindWithCountV2(
		UUID userId,
		Pageable pageable,
		LocalDateTime startBound,
		LocalDateTime endBound,
		Boolean isReadAfterRemind
	) {
		BooleanExpression baseConditions = article.user.id.eq(userId)
			.and(article.remindAt.gt(startBound).and(article.remindAt.loe(endBound)));

		BooleanExpression conditions = baseConditions.and(article.isReadAfterRemind.eq(isReadAfterRemind));

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(conditions)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(article.remindAt.asc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(conditions);

		Long readCount = queryFactory
			.select(article.count())
			.from(article)
			.where(baseConditions.and(article.isReadAfterRemind.isTrue()))
			.fetchOne();

		Long unreadCount = queryFactory
			.select(article.count())
			.from(article)
			.where(baseConditions.and(article.isReadAfterRemind.isFalse()))
			.fetchOne();

		Long totalCount = queryFactory
			.select(article.count())
			.from(article)
			.where(baseConditions)
			.fetchOne();

		return new RemindArticlesWithCountV2(
			readCount,
			unreadCount,
			totalCount,
			PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne)
		);
	}

	@Override
	public ArticleCountInfoV3 findTodayRemindCountV3(
		UUID userId,
		LocalDateTime startBound,
		LocalDateTime endBound
	) {
		BooleanExpression baseConditions = article.user.id.eq(userId)
			.and(article.remindAt.gt(startBound).and(article.remindAt.loe(endBound)));

		return queryFactory
			.select(Projections.constructor(ArticleCountInfoV3.class,
				article.count(),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = true THEN 1 ELSE 0 END)", article.isReadAfterRemind).coalesce(0L),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = false THEN 1 ELSE 0 END)", article.isReadAfterRemind).coalesce(0L)
			))
			.from(article)
			.where(baseConditions)
			.fetchOne();
	}

	@Override
	public ArticleWithCountV3 findAllByReadStatus(UUID userId, Boolean readStatus, PageRequest pageRequest) {
		BooleanExpression baseConditions = article.user.id.eq(userId);

		ArticleInfoV3 counts = queryFactory
			.select(Projections.constructor(ArticleInfoV3.class,
				article.count(),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = false THEN 1 ELSE 0 END)", article.isRead).coalesce(0L)
			))
			.from(article)
			.where(baseConditions)
			.fetchOne();

		BooleanExpression listConditions = (readStatus == null)
			? baseConditions
			: baseConditions.and(article.isRead.isFalse());

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(listConditions)
			.offset(pageRequest.getOffset())
			.limit(pageRequest.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(listConditions);

		ArticleInfoV3 safeCounts = (counts != null) ? counts : new ArticleInfoV3(0L, 0L);

		return new ArticleWithCountV3(
			safeCounts.totalCount(),
			safeCounts.unreadCount(),
			PageableExecutionUtils.getPage(articles, pageRequest, countQuery::fetchOne)
		);
	}

	@Override
	public ArticleCountInfoV3 findAllCountV3(UUID userId) {
		BooleanExpression baseConditions = article.user.id.eq(userId);

		// 쿼리 실행
		ArticleCountInfoV3 result = queryFactory
			.select(Projections.constructor(ArticleCountInfoV3.class,
				article.count(),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = true THEN 1 ELSE 0 END)", article.isRead).coalesce(0L),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = false THEN 1 ELSE 0 END)", article.isRead).coalesce(0L)
			))
			.from(article)
			.where(baseConditions)
			.fetchOne();

		return Optional.ofNullable(result)
			.orElseGet(() -> new ArticleCountInfoV3(0L, 0L, 0L));
	}

	@Override
	public ArticleWithCountV3 findAllByCategoryAndReadStatus(
		UUID userId,
		long categoryId,
		Boolean readStatus,
		PageRequest pageRequest
	) {
		BooleanExpression baseConditions = article.user.id.eq(userId)
			.and(article.category.id.eq(categoryId));

		ArticleInfoV3 counts = queryFactory
			.select(Projections.constructor(ArticleInfoV3.class,
				article.count(),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = false THEN 1 ELSE 0 END)", article.isRead).coalesce(0L)
			))
			.from(article)
			.where(baseConditions)
			.fetchOne();

		BooleanExpression listConditions = (readStatus == null)
			? baseConditions
			: baseConditions.and(article.isRead.isFalse());

		List<Article> articles = queryFactory
			.selectFrom(article)
			.join(article.user, user).fetchJoin()
			.where(listConditions)
			.offset(pageRequest.getOffset())
			.limit(pageRequest.getPageSize())
			.orderBy(article.createdAt.desc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(article.count())
			.from(article)
			.where(listConditions);

		ArticleInfoV3 safeCounts = (counts != null) ? counts : new ArticleInfoV3(0L, 0L);

		return new ArticleWithCountV3(
			safeCounts.totalCount(),
			safeCounts.unreadCount(),
			PageableExecutionUtils.getPage(articles, pageRequest, countQuery::fetchOne)
		);
	}

	@Override
	public ArticleCountInfoV3 findAllCountByCategoryV3(UUID userId, long categoryId) {
		BooleanExpression baseConditions = article.user.id.eq(userId)
			.and(article.category.id.eq(categoryId));

		ArticleCountInfoV3 result = queryFactory
			.select(Projections.constructor(ArticleCountInfoV3.class,
				article.count(),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = true THEN 1 ELSE 0 END)", article.isRead).coalesce(0L),
				Expressions.numberTemplate(Long.class,
					"SUM(CASE WHEN {0} = false THEN 1 ELSE 0 END)", article.isRead).coalesce(0L)
			))
			.from(article)
			.where(baseConditions)
			.fetchOne();

		return Optional.ofNullable(result)
			.orElseGet(() -> new ArticleCountInfoV3(0L, 0L, 0L));
	}

	@Override
	public SharedArticles findTopListByJob(Job job) {
		List<Article> articles = queryFactory.selectFrom(article)
			.join(article.user, user)
			.where(user.job.eq(job))
			.orderBy(article.createdAt.desc())
			.limit(10)
			.fetch();

		List<SharedArticle> sharedArticles = articles.stream()
			.map(SharedArticle::from)
			.toList();

		return SharedArticles.of(sharedArticles);
	}
}
