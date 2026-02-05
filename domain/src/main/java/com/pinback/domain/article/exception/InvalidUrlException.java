package com.pinback.domain.article.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class InvalidUrlException extends ApplicationException {
	public InvalidUrlException() {
		super(ExceptionCode.INVALID_URL);
	}
}
