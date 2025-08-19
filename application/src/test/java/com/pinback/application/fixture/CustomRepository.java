package com.pinback.application.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@Repository
public class CustomRepository {
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void clearAndReset() {
		entityManager.createNativeQuery("DELETE FROM article_migration").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM push_subscription_migration").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM category_migration").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM users_migration").executeUpdate();

		entityManager.createNativeQuery("ALTER TABLE article_migration ALTER COLUMN article_id RESTART WITH 1")
			.executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE category_migration ALTER COLUMN category_id RESTART WITH 1")
			.executeUpdate();
	}
}
