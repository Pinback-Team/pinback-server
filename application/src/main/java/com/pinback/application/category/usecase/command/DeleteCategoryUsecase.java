package com.pinback.application.category.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.service.ArticleDeleteService;
import com.pinback.application.category.port.in.DeleteCategoryPort;
import com.pinback.application.category.port.out.CategoryDeleteServicePort;
import com.pinback.application.category.port.out.CategoryGetServicePort;
import com.pinback.application.common.exception.CategoryNotOwnedException;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCategoryUsecase implements DeleteCategoryPort {

	private final CategoryGetServicePort categoryGetServicePort;
	private final CategoryDeleteServicePort categoryDeleteServicePort;

	private final ArticleDeleteService articleDeleteService;

	@Override
	public void deleteCategory(User user, long categoryId) {
		Category category = categoryGetServicePort.findById(categoryId);
		checkOwner(category, user);

		articleDeleteService.deleteByCategory(user, categoryId);
		categoryDeleteServicePort.delete(category);
	}

	private void checkOwner(Category category, User user) {
		if (!category.getUser().equals(user)) {
			throw new CategoryNotOwnedException();
		}
	}
}
