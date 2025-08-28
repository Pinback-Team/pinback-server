package com.pinback.domain.notification.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class EmptyFcmTokenException extends ApplicationException {
	public EmptyFcmTokenException() {
		super(ExceptionCode.INVALID_FCM_TOKEN);
	}
}
