package com.pinback.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.constant.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
	private String code;
	private String message;
	private T data;

	public static <T> ResponseDto<T> of(SuccessCode successCode, T data) {
		return new ResponseDto<>(successCode.getCode(), successCode.getMessage(), data);
	}

	public static <T> ResponseDto<T> of(ExceptionCode exceptionCode) {
		return new ResponseDto<>(exceptionCode.getCode(), exceptionCode.getMessage(), null);
	}

	public static <T> ResponseDto<T> of(ExceptionCode exceptionCode, String message) {
		return new ResponseDto<>(exceptionCode.getCode(), message, null);
	}

	public static <T> ResponseDto<T> ok(T data) {
		return of(SuccessCode.OK, data);
	}

	public static ResponseDto<Void> ok() {
		return of(SuccessCode.NO_CONTENT, null);
	}

	public static <T> ResponseDto<T> created(T data) {
		return of(SuccessCode.CREATED, data);
	}

	public static ResponseDto<Void> created() {
		return of(SuccessCode.NO_CONTENT, null);
	}
}
