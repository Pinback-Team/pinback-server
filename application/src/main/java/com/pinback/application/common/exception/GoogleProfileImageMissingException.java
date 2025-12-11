package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class GoogleProfileImageMissingException extends ApplicationException {
	public GoogleProfileImageMissingException() {
		super(ExceptionCode.GOOGLE_PROFILE_IMAGE_MISSING);
	}
}
