package com.pinback.pinback_server.domain.user.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class UserNotFoundException extends ApplicationException {
	public UserNotFoundException() {
		super(ExceptionCode.USER_NOT_FOUND);
	}
}
