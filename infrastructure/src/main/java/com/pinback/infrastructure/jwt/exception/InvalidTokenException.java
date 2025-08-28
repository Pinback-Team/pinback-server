package com.pinback.infrastructure.jwt.exception;

import com.pinback.shared.exception.ApplicationException;
import com.pinback.shared.constant.ExceptionCode;

public class InvalidTokenException extends ApplicationException {
	public InvalidTokenException() {
		super(ExceptionCode.INVALID_TOKEN);
	}
}
