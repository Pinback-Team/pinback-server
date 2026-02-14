package com.pinback.infrastructure.redis.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.pinback.infrastructure.article.repository.dto.SharedArticle;
import com.pinback.infrastructure.article.repository.dto.SharedArticles;

@Service
public class SharedArticleRedisService {
	private static final String KEY_PREFIX = "shared:job:";
	private final RedisTemplate<String, Object> redisTemplate;

	public SharedArticleRedisService(
		@Qualifier("sharedArticleRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@SuppressWarnings("unchecked")
	public SharedArticles getSharedArticles(String jobKey) {
		Object data = redisTemplate.opsForValue().get(KEY_PREFIX + jobKey);

		if (data instanceof SharedArticles) {
			return (SharedArticles)data;
		}

		if (data instanceof List<?>) {
			return SharedArticles.of((List<SharedArticle>)data);
		}

		return SharedArticles.of(List.of());
	}

	public void setSharedArticles(String jobKey, SharedArticles articles) {
		redisTemplate.opsForValue().set(KEY_PREFIX + jobKey, articles, Duration.ofHours(24));
	}
}
