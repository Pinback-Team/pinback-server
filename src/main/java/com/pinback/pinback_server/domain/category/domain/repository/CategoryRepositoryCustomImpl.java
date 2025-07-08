package com.pinback.pinback_server.domain.category.domain.repository;

import static com.pinback.pinback_server.domain.category.domain.entity.QCategory.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForExtension;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public CategoriesForExtension findAllForExtension(UUID userId) {
		List<Category> categories = queryFactory
			.selectFrom(category)
			.where(category.user.id.eq(userId))
			.orderBy(category.id.asc()) // 생성 오래된 순
			.fetch();

		return new CategoriesForExtension(categories);
	}
}
