package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class ArticleNotFoundException extends ApplicationException {
	public ArticleNotFoundException() {
		super(ExceptionCode.ARTICLE_NOT_FOUND);
	}
}
