package com.pinback.pinback_server.domain.category.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryDeleteService {
	private final CategoryRepository categoryRepository;

	@Transactional
	public void delete(final Category category) {
		categoryRepository.delete(category);
	}
}
