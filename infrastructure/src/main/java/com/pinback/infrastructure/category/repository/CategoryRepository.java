package com.pinback.infrastructure.category.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

	Optional<Category> findByIdAndUser(long categoryId, User user);

	boolean existsByNameAndUser(String categoryName, User user);

	long countByUser(User user);

	@Query("SELECT c.color FROM Category c WHERE c.user = :user")
	Set<CategoryColor> findColorsByUser(User user);
}
