package com.pinback.pinback_server.domain.article.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class ArticleNotOwnedException extends ApplicationException {
	public ArticleNotOwnedException() {
		super(ExceptionCode.NOT_ARTICLE_OWNER);
	}
}
