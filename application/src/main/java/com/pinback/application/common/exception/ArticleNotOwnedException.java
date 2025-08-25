package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class ArticleNotOwnedException extends ApplicationException {
	public ArticleNotOwnedException() {
		super(ExceptionCode.NOT_ARTICLE_OWNER);
	}
}
