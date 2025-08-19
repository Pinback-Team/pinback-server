package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class ArticleAlreadyExistException extends ApplicationException {
	public ArticleAlreadyExistException() {
		super(ExceptionCode.ARTICLE_ALREADY_EXIST);
	}
}