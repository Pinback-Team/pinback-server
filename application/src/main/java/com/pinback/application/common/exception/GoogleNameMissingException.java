package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class GoogleNameMissingException extends ApplicationException {
	public GoogleNameMissingException() {
		super(ExceptionCode.GOOGLE_NAME_MISSING);
	}
}
