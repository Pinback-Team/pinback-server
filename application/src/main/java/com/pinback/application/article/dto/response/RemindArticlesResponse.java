package com.pinback.application.article.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RemindArticlesResponse(
	long totalElements,
	LocalDateTime nextRemindDate,
	List<ArticleResponse> articles
) {
	public static RemindArticlesResponse of(long totalElements, LocalDateTime nextRemindDate, List<ArticleResponse> articles) {
		return new RemindArticlesResponse(totalElements, nextRemindDate, articles);
	}
}