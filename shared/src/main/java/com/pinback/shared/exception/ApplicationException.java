package com.pinback.shared.exception;

import com.pinback.shared.constant.ExceptionCode;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public ApplicationException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}
}
