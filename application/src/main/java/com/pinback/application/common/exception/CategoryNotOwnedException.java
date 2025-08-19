package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class CategoryNotOwnedException extends ApplicationException {
	public CategoryNotOwnedException() {
		super(ExceptionCode.NOT_CATEGORY_OWNER);
	}
}