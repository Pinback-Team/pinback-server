package com.pinback.application.article.usecase.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.SharedArticleDto;
import com.pinback.application.article.dto.response.SharedArticleDetailResponse;
import com.pinback.application.article.dto.response.SharedArticlePageResponse;
import com.pinback.application.article.dto.response.SharedArticleResponse;
import com.pinback.application.article.port.in.GetSharedArticlePort;
import com.pinback.application.article.port.out.ArticleGetServicePort;
import com.pinback.application.article.port.out.SharedArticleRedisPort;
import com.pinback.application.common.exception.SharedArticleNotFoundException;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSharedArticleUsecase implements GetSharedArticlePort {
	private final SharedArticleRedisPort sharedArticleRedisPort;
	private final ArticleGetServicePort articleGetServicePort;

	@Override
	public SharedArticlePageResponse getAllSharedArticle(User user) {
		if (user.getJob() == null) {
			return SharedArticlePageResponse.of(null, List.of());
		}

		String jobKey = user.getJob().getKey();
		List<SharedArticleDto> articles = sharedArticleRedisPort.getSharedArticlesByJob(jobKey);

		String jobValue = user.getJob().getValue();
		if (articles == null || articles.isEmpty()) {
			return SharedArticlePageResponse.of(jobValue, List.of());
		}

		List<SharedArticleResponse> articleResponses = articles.stream()
			.map(SharedArticleResponse::from)
			.toList();
		return SharedArticlePageResponse.of(jobValue, articleResponses);
	}

	@Override
	public SharedArticleDetailResponse getSharedArticleDetail(User user, Long articleId) {
		String jobKey = user.getJob().getKey();
		List<Long> sharedIds = sharedArticleRedisPort.getSharedArticleIdsByJob(jobKey);

		if (!sharedIds.contains(articleId)) {
			throw new SharedArticleNotFoundException();
		}

		Article article = articleGetServicePort.findById(articleId);

		return SharedArticleDetailResponse.from(article);
	}
}
