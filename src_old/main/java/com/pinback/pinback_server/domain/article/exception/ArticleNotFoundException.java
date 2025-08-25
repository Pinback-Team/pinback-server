package com.pinback.pinback_server.domain.article.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class ArticleNotFoundException extends ApplicationException {
	public ArticleNotFoundException() {
		super(ExceptionCode.ARTICLE_NOT_FOUND);
	}
}
