package com.pinback.pinback_server.domain.category.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.category.domain.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
