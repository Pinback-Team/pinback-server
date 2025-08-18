package com.pinback.domain.article.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class ArticleAlreadyExistException extends ApplicationException {
	public ArticleAlreadyExistException() {
		super(ExceptionCode.ARTICLE_ALREADY_EXIST);
	}
}
