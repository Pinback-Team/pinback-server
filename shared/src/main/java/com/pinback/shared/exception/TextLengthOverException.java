package com.pinback.shared.exception;

import com.pinback.shared.constant.ExceptionCode;

public class TextLengthOverException extends ApplicationException {
	public TextLengthOverException() {
		super(ExceptionCode.TEXT_LENGTH_OVER);
	}
}
