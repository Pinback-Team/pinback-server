package com.pinback.pinback_server.global.exception;

import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public ApplicationException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}
}
