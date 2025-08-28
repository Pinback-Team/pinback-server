package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class CategoryNotFoundException extends ApplicationException {
	public CategoryNotFoundException() {
		super(ExceptionCode.CATEGORY_NOT_FOUND);
	}
}
