package com.pinback.domain.notification.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class SubscriptionNotFoundException extends ApplicationException {
	public SubscriptionNotFoundException() {
		super(ExceptionCode.SUBSCRIPTION_NOT_FOUND);
	}
}
