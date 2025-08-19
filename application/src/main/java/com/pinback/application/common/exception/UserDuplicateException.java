package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class UserDuplicateException extends ApplicationException {
	public UserDuplicateException() {
		super(ExceptionCode.USER_ALREADY_EXIST);
	}
}