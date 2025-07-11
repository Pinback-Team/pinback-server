package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.util.List;

public record RemindArticleResponse(
	long totalArticle,
	String nextRemind,
	List<RemindArticles> articles
) {
}
