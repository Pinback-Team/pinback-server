package com.pinback.pinback_server.global.exception.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	//400
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "c40000", "잘못된 요청입니다."),
	CATEGORY_LIMIT_OVER(HttpStatus.BAD_REQUEST, "c40001", "카테고리는 최대 10개까지 생성할 수 있습니다."),
	TEXT_LENGTH_OVER(HttpStatus.BAD_REQUEST, "c40002", "글자 수 제한을 초과하였습니다."),
	CATEGORY_NAME_INVALID(HttpStatus.BAD_REQUEST, "c40003", "카테고리 이름은 공백을 포함할 수 없습니다."),

	//401
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "c40101", "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "c40102", "만료된 토큰입니다."),
	EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "c40101", "토큰이 비어있습니다."),

	//403
	NOT_ARTICLE_OWNER(HttpStatus.FORBIDDEN, "c40301", "아티클에 대한 소유권이 없습니다."),
	NOT_CATEGORY_OWNER(HttpStatus.FORBIDDEN, "c40302", "카테고리에 대한 소유권이 없습니다."),

	//404
	NOT_FOUND(HttpStatus.NOT_FOUND, "c40400", "리소스가 존재하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "c40401", "사용자가 존재하지 않습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "c40402", "카테고리가 존재하지 않습니다."),
	ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "c40403", "아티클이 존재하지 않습니다."),

	//405
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "c40500", "잘못된 HTTP method 요청입니다."),

	//409
	DUPLICATE(HttpStatus.CONFLICT, "c40900", "이미 존재하는 리소스입니다."),
	USER_ALREADY_EXIST(HttpStatus.CONFLICT, "c40901", "이미 존재하는 사용자입니다."),
	ARTICLE_ALREADY_EXIST(HttpStatus.CONFLICT, "c40902", "이미 저장한 url 입니다."),
	CATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "c40903", "이미 존재하는 카테고리입니다."),

	//500
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "s50000", "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ExceptionCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

}

