package com.pinback.application.article.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.AcornCollectResult;
import com.pinback.application.article.dto.response.ReadArticleResponse;
import com.pinback.application.article.port.in.UpdateArticleStatusPort;
import com.pinback.application.article.service.ArticleGetServicePort;
import com.pinback.application.user.port.out.AcornServicePort;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateArticleStatusUsecase implements UpdateArticleStatusPort {

	private final ArticleGetServicePort articleGetService;
	private final AcornServicePort acornService;

	@Override
	public ReadArticleResponse updateArticleStatus(User user, long articleId) {
		Article article = articleGetService.findByUserAndId(user, articleId);

		int currentAcorns = acornService.getCurrentAcorns(user.getId());
		log.info("수집하기 전 도토리 수: {}", currentAcorns);

		if (!article.isRead()) {
			article.markAsRead();
			AcornCollectResult result = acornService.tryCollectAcorns(user);
			return ReadArticleResponse.of(result.finalAcornCount(), result.isCollected());
		}

		return ReadArticleResponse.of(currentAcorns, false);
	}
}
