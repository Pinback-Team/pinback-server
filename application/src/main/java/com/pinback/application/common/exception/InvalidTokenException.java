package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class InvalidTokenException extends ApplicationException {
	public InvalidTokenException() {
		super(ExceptionCode.INVALID_TOKEN);
	}
}
