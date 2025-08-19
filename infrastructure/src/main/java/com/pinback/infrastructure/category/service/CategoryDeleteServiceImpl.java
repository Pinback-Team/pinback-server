package com.pinback.infrastructure.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.service.CategoryDeleteService;
import com.pinback.domain.category.entity.Category;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryDeleteServiceImpl implements CategoryDeleteService {

	private final CategoryRepository categoryRepository;

	@Override
	public void delete(Category category) {
		categoryRepository.delete(category);
	}
}