package com.pinback.pinback_server.global.exception;

import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

public class TextLengthOverException extends ApplicationException {
	public TextLengthOverException() {
		super(ExceptionCode.TEXT_LENGTH_OVER);
	}
}
