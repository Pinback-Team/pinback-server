package com.pinback.pinback_server.global.common.jwt.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class ExpiredTokenException extends ApplicationException {
	public ExpiredTokenException() {
		super(ExceptionCode.EXPIRED_TOKEN);
	}
}
