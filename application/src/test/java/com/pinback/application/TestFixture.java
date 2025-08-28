package com.pinback.application;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

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

	public static Category categoryWithName(User user, String name) {
		return Category.create(name, user);
	}

	public static Article article(User user) {
		Category category = category(user);
		return Article.create("https://test.com", "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article articleWithCategory(User user, Category category) {
		return Article.create("https://test.com", "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article articleWithUrl(User user, String url, Category category) {
		return Article.create(url, "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
	}

	public static Article articleWithDate(User user, String url, Category category, LocalDateTime remindAt) {
		return Article.create(url, "testmemo", user, category, remindAt);
	}

	public static Article readArticle(User user, String url, Category category) {
		Article article = Article.create(url, "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
		article.markAsRead();
		return article;
	}

	public static PushSubscription pushSubscription(User user) {
		return PushSubscription.create(user, "testToken");
	}

	public static PushSubscription pushSubscriptionWithToken(User user, String token) {
		return PushSubscription.create(user, token);
	}
}
