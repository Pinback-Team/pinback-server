package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class GoogleTokenMissingException extends ApplicationException {
	public GoogleTokenMissingException() {
		super(ExceptionCode.GOOGLE_TOKEN_MISSING);
	}
}
