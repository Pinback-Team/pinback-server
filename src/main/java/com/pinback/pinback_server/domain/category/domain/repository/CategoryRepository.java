package com.pinback.pinback_server.domain.category.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByIdAndUser(long categoryId, User user);

	boolean existsByNameAndUser(String categoryName, User user);

	long countByUser(User user);
}
