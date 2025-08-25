package com.pinback.pinback_server.domain.article.domain.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticlesWithUnreadCount {
	private Long unReadCount;
	private Page<Article> article;

	@QueryProjection
	public ArticlesWithUnreadCount(Long unReadCount, Page<Article> article) {
		this.unReadCount = unReadCount;
		this.article = article;
	}
}
