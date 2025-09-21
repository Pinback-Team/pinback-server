package com.pinback.infrastructure.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

	Optional<Category> findByIdAndUser(long categoryId, User user);

	boolean existsByNameAndUser(String categoryName, User user);

	long countByUser(User user);

	void deleteAllByUser(User user);
}
