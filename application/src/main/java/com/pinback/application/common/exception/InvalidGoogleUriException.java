package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class InvalidGoogleUriException extends ApplicationException {
	public InvalidGoogleUriException() {
		super(ExceptionCode.INVALID_REDIRECT_URI);
	}
}
