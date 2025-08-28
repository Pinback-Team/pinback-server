package com.pinback.application.article.dto.query;

public record PageQuery(
	int pageNumber,
	int pageSize
) {
	public static PageQuery of(int pageNumber, int pageSize) {
		return new PageQuery(pageNumber, pageSize);
	}
}
