package com.pinback.application.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.exception.CategoryNotFoundException;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryGetService {

	private final CategoryRepository categoryRepository;

	public Category findById(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(CategoryNotFoundException::new);
	}

	public List<Category> findAllByUser(User user) {
		return categoryRepository.findByUser(user);
	}

	public Category findByIdAndUserId(Long categoryId, User user) {
		return categoryRepository.findByIdAndUser(categoryId, user)
			.orElseThrow(CategoryNotFoundException::new);
	}

	public boolean existsByNameAndUserId(String name, UUID userId) {
		return categoryRepository.existsByNameAndUserId(name, userId);
	}

	public long countByUserId(UUID userId) {
		return categoryRepository.countByUserId(userId);
	}
}
