package com.pinback.pinback_server.domain.fixture;

import java.time.LocalTime;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;

public class TestFixture {

	public static User user() {
		return User.create("testUser@gmail.com", LocalTime.of(12, 0, 0));
	}

	public static Category category(User user) {
		return Category.create("테스트카테고리", user);
	}
}
