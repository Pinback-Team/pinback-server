package com.pinback.pinback_server.domain.article.domain.repository;

import static com.pinback.pinback_server.domain.article.domain.entity.QArticle.*;
import static com.pinback.pinback_server.domain.user.domain.entity.QUser.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Article> findAllCustom(UUID userId, Pageable pageable) {
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

		return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
	}
}
