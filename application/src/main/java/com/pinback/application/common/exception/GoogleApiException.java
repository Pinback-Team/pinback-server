package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class GoogleApiException extends ApplicationException {
	public GoogleApiException() {
		super(ExceptionCode.GOOGLE_API_ERROR);
	}
}
