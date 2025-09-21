package com.pinback.infrastructure.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.port.out.CategoryDeleteServicePort;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryDeleteService implements CategoryDeleteServicePort {

	private final CategoryRepository categoryRepository;

	@Override
	public void delete(Category category) {
		categoryRepository.delete(category);
	}

	@Override
	public void deleteAllByUser(User user) {
		categoryRepository.deleteAllByUser(user);
	}
}
