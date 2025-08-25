package com.pinback.pinback_server.domain.notification.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class SubscriptionNotFoundException extends ApplicationException {
	public SubscriptionNotFoundException() {
		super(ExceptionCode.SUBSCRIPTION_NOT_FOUND);
	}
}
