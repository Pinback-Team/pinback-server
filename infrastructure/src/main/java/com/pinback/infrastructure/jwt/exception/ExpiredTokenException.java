package com.pinback.infrastructure.jwt.exception;

import com.pinback.shared.exception.ApplicationException;
import com.pinback.shared.constant.ExceptionCode;

public class ExpiredTokenException extends ApplicationException {
	public ExpiredTokenException() {
		super(ExceptionCode.EXPIRED_TOKEN);
	}
}
