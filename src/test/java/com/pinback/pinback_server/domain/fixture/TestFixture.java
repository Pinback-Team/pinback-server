package com.pinback.pinback_server.domain.fixture;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;

public class TestFixture {

	public static User user() {
		return User.create("testUser@gmail.com", LocalTime.of(12, 0, 0));
	}

	public static User userWithEmail(String email) {
		return User.create(email, LocalTime.of(12, 0, 0));
	}

	public static Category category(User user) {
		return Category.create("테스트카테고리", user);
	}

	public static Article article(User user) {
		Category category = category(user);
		return Article.create("test", "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article articleWithCategory(User user, Category category) {
		return Article.create("test", "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article article(User user, String url, Category category) {
		return Article.create(url, "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article readArticle(User user, String url, Category category) {
		Article article = Article.create(url, "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
		article.toRead();
		return article;
	}
}
