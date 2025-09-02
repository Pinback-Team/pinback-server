package com.pinback.domain.category.exception;

import static com.pinback.shared.constant.ExceptionCode.*;

import com.pinback.shared.exception.ApplicationException;

public class CategoryNameLengthOverException extends ApplicationException {
	public CategoryNameLengthOverException() {
		super(TEXT_LENGTH_OVER);
	}
}
