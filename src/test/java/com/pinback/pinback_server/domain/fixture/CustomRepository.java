package com.pinback.pinback_server.domain.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class CustomRepository {
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void clearAndReset() {
		entityManager.createNativeQuery("DELETE FROM article").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM category").executeUpdate();
		entityManager.createNativeQuery("DELETE FROM users").executeUpdate();

		entityManager.createNativeQuery("ALTER TABLE article ALTER COLUMN article_id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE category ALTER COLUMN category_id RESTART WITH 1").executeUpdate();
	}
}
