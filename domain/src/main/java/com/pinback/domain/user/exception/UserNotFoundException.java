package com.pinback.domain.user.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
	public UserNotFoundException() {
		super(ExceptionCode.USER_NOT_FOUND);
	}
}
