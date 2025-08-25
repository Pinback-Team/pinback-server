package com.pinback.pinback_server.domain.article.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class MemoLengthLimitException extends ApplicationException {
	public MemoLengthLimitException() {
		super(ExceptionCode.TEXT_LENGTH_OVER);
	}
}
