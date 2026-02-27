package com.pinback.infrastructure.redis.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pinback.application.article.dto.SharedArticleDto;
import com.pinback.application.article.port.out.SharedArticleRedisPort;
import com.pinback.infrastructure.article.repository.dto.SharedArticle;
import com.pinback.infrastructure.article.repository.dto.SharedArticles;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SharedArticleRedisAdapter implements SharedArticleRedisPort {
	private final SharedArticleRedisService sharedArticleRedisService;

	@Override
	public List<SharedArticleDto> getSharedArticlesByJob(String job) {
		SharedArticles sharedArticles = sharedArticleRedisService.getSharedArticles(job);

		if (sharedArticles == null || sharedArticles.articles() == null) {
			return List.of();
		}

		return sharedArticles.articles().stream()
			.map(infraDto -> new SharedArticleDto(
				infraDto.articleId(),
				infraDto.url(),
				infraDto.title(),
				infraDto.thumbnailUrl(),
				infraDto.memo(),
				infraDto.ownerName(),
				infraDto.categoryId(),
				infraDto.categoryName(),
				infraDto.categoryColor()
			))
			.toList();

	}

	@Override
	public List<Long> getSharedArticleIdsByJob(String job) {
		SharedArticles sharedArticles = sharedArticleRedisService.getSharedArticles(job);

		if (sharedArticles == null || sharedArticles.articles() == null) {
			return List.of();
		}

		return sharedArticles.articles().stream()
			.map(SharedArticle::articleId)
			.toList();
	}

	@Override
	public void saveSharedArticles(String job, List<SharedArticleDto> articles) {

		List<SharedArticle> infraEntities = articles.stream()
			.map(dto -> new SharedArticle(
				dto.articleId(),
				dto.url(),
				dto.title(),
				dto.thumbnailUrl(),
				dto.memo(),
				dto.ownerName(),
				dto.categoryId(),
				dto.categoryName(),
				dto.categoryColor()
			))
			.toList();

		sharedArticleRedisService.setSharedArticles(job, SharedArticles.of(infraEntities));
	}
}
