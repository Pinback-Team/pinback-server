package com.pinback.infrastructure.fixture;

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
		entityManager.clear();
		
		entityManager.createNativeQuery("DELETE FROM article").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM push_subscription").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM category").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM users").executeUpdate();

		entityManager.createNativeQuery("ALTER TABLE article ALTER COLUMN article_id RESTART WITH 1")
			.executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE category ALTER COLUMN category_id RESTART WITH 1")
			.executeUpdate();
	}
}
