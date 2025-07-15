package com.pinback.pinback_server.domain.article.presentation.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.pinback.pinback_server.domain.article.domain.entity.Article;

public record RemindArticles(
	long articleId,
	String url,
	String memo,
	String remindAt,
	boolean isRead
) {
	public static RemindArticles from(Article article) {
		return new RemindArticles(
			article.getId(),
			article.getUrl(),
			article.getMemo(),
			article.getRemindAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a HH시 mm분", Locale.KOREAN)),
			article.isRead()
		);
	}
}
