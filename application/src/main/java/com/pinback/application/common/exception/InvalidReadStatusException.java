package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class InvalidReadStatusException extends ApplicationException {
	public InvalidReadStatusException() {
		super(ExceptionCode.INVALID_READSTATUS);
	}
}
