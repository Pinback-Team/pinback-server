package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class GoogleEmailMissingException extends ApplicationException {
	public GoogleEmailMissingException() {
		super(ExceptionCode.GOOGLE_EMAIL_MISSING);
	}
}
