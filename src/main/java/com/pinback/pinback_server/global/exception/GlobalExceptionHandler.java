package com.pinback.pinback_server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pinback.pinback_server.global.common.dto.ResponseDto;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ResponseDto<Void>> handleBusinessException(ApplicationException ex) {
		ExceptionCode exceptionCode = ex.getExceptionCode();
		return ResponseEntity
			.status(exceptionCode.getStatus())
			.body(ResponseDto.of(exceptionCode));
	}
}
