package com.pinback.infrastructure.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.dto.CategoriesForDashboardDto;
import com.pinback.application.category.dto.CategoryForDashboardDto;
import com.pinback.application.category.service.CategoryGetService;
import com.pinback.application.common.exception.CategoryNotFoundException;
import com.pinback.application.common.exception.CategoryNotOwnedException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryGetServiceImpl implements CategoryGetService {

	private final CategoryRepository categoryRepository;

	@Override
	public Category findById(long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
	}

	@Override
	public Category getCategoryAndUser(long categoryId, User user) {
		Category category = findById(categoryId);
		if (!category.getUser().equals(user)) {
			throw new CategoryNotOwnedException();
		}
		return category;
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
		return categoryRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
	}

	@Override
	public CategoriesForDashboardDto findAllForDashboard(UUID userId) {
		// TODO: 복잡한 쿼리 구현 필요 - 일단 기본 카테고리 목록만 반환
		List<Category> categories = categoryRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
		List<CategoryForDashboardDto> categoryDtos = categories.stream()
			.map(category -> new CategoryForDashboardDto(
				category.getId(), 
				category.getName(), 
				0L // 임시로 0으로 설정
			))
			.toList();
		
		return new CategoriesForDashboardDto(categoryDtos);
	}
}