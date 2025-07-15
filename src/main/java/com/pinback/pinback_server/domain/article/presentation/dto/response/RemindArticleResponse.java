package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RemindArticleResponse(
	long totalArticle,
	LocalDateTime nextRemind,
	List<RemindArticles> articles
) {
}
