package com.pinback.application.article.port.out;

import java.util.List;

import com.pinback.application.article.dto.SharedArticleDto;

public interface SharedArticleRedisPort {
	List<SharedArticleDto> getSharedArticlesByJob(String job);

	List<Long> getSharedArticleIdsByJob(String job);

	void saveSharedArticles(String job, List<SharedArticleDto> articles);
}
