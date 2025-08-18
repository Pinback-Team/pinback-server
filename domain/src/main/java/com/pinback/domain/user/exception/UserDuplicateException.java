package com.pinback.domain.user.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class UserDuplicateException extends ApplicationException {
	public UserDuplicateException() {
		super(ExceptionCode.USER_ALREADY_EXIST);
	}
}