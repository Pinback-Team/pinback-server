package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class SharedArticleNotFoundException extends ApplicationException {
	public SharedArticleNotFoundException() {
		super(ExceptionCode.SHARED_ARTICLE_NOT_FOUND);
	}
}
