package com.pinback.pinback_server.domain.article.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class ArticleAlreadyExistException extends ApplicationException {
	public ArticleAlreadyExistException() {
		super(ExceptionCode.ARTICLE_ALREADY_EXIST);
	}
}
