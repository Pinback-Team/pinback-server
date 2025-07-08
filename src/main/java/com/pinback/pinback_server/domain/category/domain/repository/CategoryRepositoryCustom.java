package com.pinback.pinback_server.domain.category.domain.repository;

import java.util.UUID;

import com.pinback.pinback_server.domain.category.domain.repository.dto.CategoriesForExtension;

public interface CategoryRepositoryCustom {
	CategoriesForExtension findAllForExtension(UUID userId);
}
