package com.pinback.pinback_server.domain.category.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class CategoryNotFoundException extends ApplicationException {
	public CategoryNotFoundException() {
		super(ExceptionCode.CATEGORY_NOT_FOUND);
	}
}
