package com.pinback.infrastructure.category.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.pinback.application.category.port.out.CategoryColorServicePort;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryColorService implements CategoryColorServicePort {

	private final CategoryRepository categoryRepository;

	@Override
	public Set<CategoryColor> getUsedColorsByUser(User user) {
		return categoryRepository.findColorsByUser(user);
	}
}
