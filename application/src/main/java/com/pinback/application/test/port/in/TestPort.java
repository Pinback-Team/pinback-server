package com.pinback.application.test.port.in;

import com.pinback.application.test.dto.request.PushTestRequest;
import com.pinback.application.test.dto.response.CategoriesTestResponse;
import com.pinback.domain.user.entity.User;

public interface TestPort {
	void pushTest(PushTestRequest request);

	void createArticlesByCategory(User user, Long categoryId);

	CategoriesTestResponse createCategories(User user);

	void deleteArticlesByCategory(User user, Long categoryId);
}
