package com.pinback.infrastructure.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.service.CategorySaveService;
import com.pinback.domain.category.entity.Category;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorySaveServiceImpl implements CategorySaveService {

	private final CategoryRepository categoryRepository;

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
}