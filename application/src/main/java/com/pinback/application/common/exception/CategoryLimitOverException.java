package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class CategoryLimitOverException extends ApplicationException {
	public CategoryLimitOverException() {
		super(ExceptionCode.CATEGORY_LIMIT_OVER);
	}
}