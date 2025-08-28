package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class FcmTokenDuplicationException extends ApplicationException {
	public FcmTokenDuplicationException() {
		super(ExceptionCode.EXPIRED_TOKEN);
	}
}
