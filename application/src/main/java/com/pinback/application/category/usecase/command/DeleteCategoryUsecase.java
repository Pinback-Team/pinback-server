package com.pinback.application.category.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.category.port.in.DeleteCategoryPort;
import com.pinback.application.category.port.out.ArticleRepositoryPort;
import com.pinback.application.category.port.out.CategoryRepositoryPort;
import com.pinback.domain.category.entity.Category;
import com.pinback.application.common.exception.CategoryNotOwnedException;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCategoryUsecase implements DeleteCategoryPort {
	
	private final CategoryRepositoryPort categoryRepositoryPort;
	private final ArticleRepositoryPort articleRepositoryPort;

	@Override
	public void deleteCategory(User user, long categoryId) {
		Category category = categoryRepositoryPort.findById(categoryId);
		validateOwnership(category, user);
		
		articleRepositoryPort.deleteByCategory(user, categoryId);
		categoryRepositoryPort.delete(category);
	}
	
	private void validateOwnership(Category category, User user) {
		if (!category.getUser().equals(user)) {
			throw new CategoryNotOwnedException();
		}
	}
}