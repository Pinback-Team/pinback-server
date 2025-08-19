package com.pinback.application.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryDeleteService {

	private final CategoryRepository categoryRepository;

	public void deleteById(Long categoryId) {
		categoryRepository.deleteById(categoryId);
	}
}
