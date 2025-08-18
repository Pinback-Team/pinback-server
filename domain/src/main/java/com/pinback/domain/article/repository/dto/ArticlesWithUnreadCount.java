package com.pinback.domain.article.repository.dto;

import org.springframework.data.domain.Page;

import com.pinback.domain.article.entity.Article;
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
