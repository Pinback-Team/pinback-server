package com.pinback.pinback_server.domain.auth.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class UserDuplicateException extends ApplicationException {
	public UserDuplicateException() {
		super(ExceptionCode.USER_ALREADY_EXIST);
	}
}
