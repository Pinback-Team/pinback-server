package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class MemoLengthLimitException extends ApplicationException {
	public MemoLengthLimitException() {
		super(ExceptionCode.TEXT_LENGTH_OVER);
	}
}