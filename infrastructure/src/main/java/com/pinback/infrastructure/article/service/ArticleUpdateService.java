package com.pinback.infrastructure.article.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.response.ArticleMetadataResponse;
import com.pinback.application.test.port.out.ArticleMetadataPort;
import com.pinback.domain.article.entity.Article;
import com.pinback.infrastructure.article.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleUpdateService {
	private final ArticleRepository articleRepository;
	private final ArticleMetadataPort articleMetadataPort;

	@Async
	public void migrateMissingMetadata() {
		// 1. 제목이 null인 기존 아티클들을 모두 가져옴
		List<Article> targetArticles = articleRepository.findByTitleIsNull();
		log.info("비동기 마이그레이션 대상 아티클 개수: {}", targetArticles.size());

		for (Article article : targetArticles) {
			try {
				// 2. URL로 메타데이터 추출 (이 과정에서 S3 업로드도 일어남)
				ArticleMetadataResponse metadata = articleMetadataPort.extractMetadata(article.getUrl());

				// 3. 엔티티 필드 업데이트
				article.updateMetadata(metadata.title(), metadata.thumbnailUrl());

				log.info("업데이트 완료: [ID: {}] -> {}", article.getId(), metadata.title());
				Thread.sleep(200);

			} catch (Exception e) {
				log.error("업데이트 중 오류 발생: [ID: {}] URL: {}", article.getId(), article.getUrl());
			}
		}
		log.info("비동기 마이그레이션 모든 작업 완료");
	}
}
