package com.pinback.application.article.dto.response;

import java.util.List;

public record SharedArticlePageResponse(
	String job,
	List<SharedArticleResponse> articles
) {
	public static SharedArticlePageResponse of(String job, List<SharedArticleResponse> articles) {
		return new SharedArticlePageResponse(job, articles);
	}
}
