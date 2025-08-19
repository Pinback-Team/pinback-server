package com.pinback.application.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.category.entity.Category;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorySaveService {

	private final CategoryRepository categoryRepository;

	public Category save(Category category) {
		return categoryRepository.save(category);
	}
}
