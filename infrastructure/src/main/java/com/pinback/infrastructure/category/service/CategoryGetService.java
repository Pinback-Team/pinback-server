package com.pinback.infrastructure.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.application.category.dto.CategoryForDashboardDto;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.CategoryNotFoundException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;
import com.pinback.infrastructure.category.repository.dto.CategoriesForDashboard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryGetService implements CategoryGetServicePort {

	private final CategoryRepository categoryRepository;

	@Override
	public Category findById(long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
	}

	@Override
	public Category getCategoryAndUser(long categoryId, User user) {
		return categoryRepository.findByIdAndUser(categoryId, user).orElseThrow(CategoryNotFoundException::new);
	}

	@Override
	public long countCategoriesByUser(User user) {
		return categoryRepository.countByUser(user);
	}

	@Override
	public boolean checkExistsByCategoryNameAndUser(String categoryName, User user) {
		return categoryRepository.existsByNameAndUser(categoryName, user);
	}

	@Override
	public List<Category> findAllForExtension(UUID userId) {
		return categoryRepository.findAllForExtension(userId);
	}

	@Override
	public CategoriesForDashboardDto findAllForDashboard(UUID userId) {
		CategoriesForDashboard result = categoryRepository.findAllForDashboard(userId);
		return convertToDto(result);
	}

	private CategoriesForDashboardDto convertToDto(CategoriesForDashboard categories) {
		List<CategoryForDashboardDto> categoryForDashboards = categories.getCategories().stream().map(category ->
		{
			return new CategoryForDashboardDto(
				category.getId(),
				category.getName(),
				category.getUnreadCount()
			);
		}).toList();

		return new CategoriesForDashboardDto(categoryForDashboards);
	}
}
