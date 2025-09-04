package com.pinback.infrastructure.category.repository;

import static com.pinback.domain.article.entity.QArticle.*;
import static com.pinback.domain.category.entity.QCategory.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.dto.CategoriesForDashboard;
import com.pinback.infrastructure.category.repository.dto.CategoryForDashboard;
import com.pinback.infrastructure.category.repository.dto.QCategoryForDashboard;
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
			.orderBy(category.id.asc())
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
	
	@Override
	public Set<CategoryColor> findColorsByUser(User user) {
		List<CategoryColor> colors = queryFactory
			.select(category.color)
			.from(category)
			.where(category.user.eq(user))
			.fetch();
		
		return new HashSet<>(colors);
	}
}
