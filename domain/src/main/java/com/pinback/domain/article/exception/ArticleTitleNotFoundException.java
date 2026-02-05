package com.pinback.domain.article.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class ArticleTitleNotFoundException extends ApplicationException {
	public ArticleTitleNotFoundException() {
		super(ExceptionCode.ARTICLE_TILE_NOT_FOUND);
	}
}
