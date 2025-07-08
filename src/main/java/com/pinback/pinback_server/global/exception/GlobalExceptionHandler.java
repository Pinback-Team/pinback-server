package com.pinback.pinback_server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pinback.pinback_server.global.common.dto.ResponseDto;
import com.pinback.pinback_server.global.exception.constant.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ResponseDto<Void>> handleBusinessException(ApplicationException ex) {
		ExceptionCode exceptionCode = ex.getExceptionCode();
		return ResponseEntity
			.status(exceptionCode.getStatus())
			.body(ResponseDto.of(exceptionCode));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ResponseDto<Void>> handleNoHandlerFoundException() {
		return ResponseEntity
			.status(ExceptionCode.NOT_FOUND.getStatus())
			.body(ResponseDto.of(ExceptionCode.NOT_FOUND));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseDto<Void>> handleHttpRequestMethodNotSupportedException() {
		return ResponseEntity
			.status(ExceptionCode.METHOD_NOT_ALLOWED.getStatus())
			.body(ResponseDto.of(ExceptionCode.METHOD_NOT_ALLOWED));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDto<Void>> handleException(Exception e) {
		log.info(e.getMessage());
		return ResponseEntity
			.status(ExceptionCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ResponseDto.of(ExceptionCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDto<Void>> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e) {

		BindingResult bindingResult = e.getBindingResult();

		String errorMessage = bindingResult.getFieldErrors()
			.stream()
			.findFirst()
			.map(FieldError::getDefaultMessage)
			.orElse("유효하지 않은 입력입니다.");

		return ResponseEntity.badRequest()
			.body(ResponseDto.of(ExceptionCode.INVALID_INPUT_VALUE, errorMessage));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseDto<Void>> handleJsonParse(HttpMessageNotReadableException ex) {
		Throwable cause = ex.getMostSpecificCause();

		if (cause instanceof ApplicationException applicationException) {
			ExceptionCode exceptionCode = applicationException.getExceptionCode();
			return ResponseEntity
				.status(exceptionCode.getStatus())
				.body(ResponseDto.of(exceptionCode));
		}

		return ResponseEntity
			.status(ExceptionCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ResponseDto.of(ExceptionCode.INTERNAL_SERVER_ERROR));
	}
}
