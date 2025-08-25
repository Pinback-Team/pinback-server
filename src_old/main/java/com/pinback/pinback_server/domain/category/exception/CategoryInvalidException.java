package com.pinback.pinback_server.domain.category.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class CategoryInvalidException extends ApplicationException {
	public CategoryInvalidException() {
		super(ExceptionCode.CATEGORY_NAME_INVALID);
	}
}
