package com.pinback.pinback_server.domain.category.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForDashboard;
import com.pinback.pinback_server.domain.category.exception.CategoryNotFoundException;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryGetService {
	private final CategoryRepository categoryRepository;

	public Category getCategoryAndUser(long categoryId, User user) {
		return categoryRepository.findByIdAndUser(categoryId, user).orElseThrow(CategoryNotFoundException::new);
	}

	public boolean checkExistsByCategoryNameAndUser(String categoryName, User user) {
		return categoryRepository.existsByNameAndUser(categoryName, user);
	}

	public long countCategoriesByUser(User user) {
		return categoryRepository.countByUser(user);
	}

	public List<Category> findAllForExtension(UUID userId) {
		return categoryRepository.findAllForExtension(userId);
	}

	public CategoriesForDashboard findAllForDashboard(UUID userId) {
		return categoryRepository.findAllForDashboard(userId);
	}
}
