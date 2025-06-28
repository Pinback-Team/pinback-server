package com.pinback.pinback_server.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pinback.pinback_server.global.common.constant.SuccessCode;

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
