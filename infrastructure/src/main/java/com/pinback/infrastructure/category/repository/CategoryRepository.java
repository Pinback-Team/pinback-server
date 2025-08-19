package com.pinback.infrastructure.category.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByUser(User userId);

	Optional<Category> findByIdAndUser(Long id, User user);

	boolean existsByNameAndUserId(String name, UUID userId);

	long countByUserId(UUID userId);
}
