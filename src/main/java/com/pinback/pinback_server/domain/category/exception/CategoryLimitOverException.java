package com.pinback.pinback_server.domain.category.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class CategoryLimitOverException extends ApplicationException {
	public CategoryLimitOverException() {
		super(ExceptionCode.CATEGORY_LIMIT_OVER);

	}
}
