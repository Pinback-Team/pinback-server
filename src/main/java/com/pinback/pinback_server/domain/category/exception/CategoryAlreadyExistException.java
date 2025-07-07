package com.pinback.pinback_server.domain.category.exception;

import com.pinback.pinback_server.global.exception.ApplicationException;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class CategoryAlreadyExistException extends ApplicationException {
	public CategoryAlreadyExistException() {
		super(ExceptionCode.CATEGORY_ALREADY_EXIST);
	}
}
