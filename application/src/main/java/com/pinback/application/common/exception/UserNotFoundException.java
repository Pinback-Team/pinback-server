package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
	public UserNotFoundException() {
		super(ExceptionCode.USER_NOT_FOUND);
	}
}