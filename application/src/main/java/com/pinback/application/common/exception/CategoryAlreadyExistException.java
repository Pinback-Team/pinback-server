package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class CategoryAlreadyExistException extends ApplicationException {
	public CategoryAlreadyExistException() {
		super(ExceptionCode.CATEGORY_ALREADY_EXIST);
	}
}