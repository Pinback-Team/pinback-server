package com.pinback.infrastructure.article.repository.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public record SharedArticles(
	List<SharedArticle> articles
) {
	public static SharedArticles of(List<SharedArticle> articles) {
		return new SharedArticles(articles);
	}
}
