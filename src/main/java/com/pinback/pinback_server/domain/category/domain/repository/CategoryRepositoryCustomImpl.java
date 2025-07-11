package com.pinback.pinback_server.domain.category.domain.repository;

import static com.pinback.pinback_server.domain.article.domain.entity.QArticle.*;
import static com.pinback.pinback_server.domain.category.domain.entity.QCategory.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForDashboard;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoryForDashboard;
import com.pinback.pinback_server.domain.category.domain.repository.dto.QCategoryForDashboard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Category> findAllForExtension(UUID userId) {
		List<Category> categories = queryFactory
			.selectFrom(category)
			.where(category.user.id.eq(userId))
			.orderBy(category.id.asc()) // 생성 오래된 순
			.fetch();

		return categories;
	}

	@Override
	public CategoriesForDashboard findAllForDashboard(UUID userId) {
		List<CategoryForDashboard> categories = queryFactory
			.select(new QCategoryForDashboard(
				category.id, category.name, article.id.count().as("unreadCount")))
			.from(category)
			.leftJoin(article)
			.on(article.category.eq(category)
				.and(article.user.id.eq(userId))
				.and(article.isRead.isFalse()))
			.where(category.user.id.eq(userId))
			.groupBy(category.id, category.name)
			.orderBy(category.id.asc())
			.fetch();

		return new CategoriesForDashboard(categories);
	}
}
