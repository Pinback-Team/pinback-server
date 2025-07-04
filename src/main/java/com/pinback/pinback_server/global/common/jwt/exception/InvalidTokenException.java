package com.pinback.pinback_server.global.common.jwt.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class InvalidTokenException extends ApplicationException {
	public InvalidTokenException() {
		super(ExceptionCode.UNAUTHORIZED);
	}
}
