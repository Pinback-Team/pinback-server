package com.pinback.domain.user.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class JobNotFoundException extends ApplicationException {
	public JobNotFoundException() {
		super(ExceptionCode.JOB_NOT_FOUND);
	}
}
