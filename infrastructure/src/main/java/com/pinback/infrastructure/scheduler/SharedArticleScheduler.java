package com.pinback.infrastructure.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.SharedArticleDto;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.article.port.out.SharedArticleRedisPort;
import com.pinback.domain.user.enums.Job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SharedArticleScheduler {
	private final SharedArticleRedisPort sharedArticleRedisPort;
	private final ArticleGetServicePort articleGetServicePort;

	@Scheduled(cron = "0 8 * * * *")
	@Transactional(readOnly = true)
	public void refreshDailySharedArticles() {
		log.info("스케줄러 실행: 직무별 공유 아티클 갱신 시작");

		for (Job job : Job.values()) {
			List<SharedArticleDto> articles = articleGetServicePort.findTopListByJob(job);

			sharedArticleRedisPort.saveSharedArticles(job.getKey(), articles);
			log.info("직무 [{}] 아티클 {}개 갱신 완료", job.getValue(), articles.size());
		}

		log.info("스케줄러 실행 완료");
	}
}
