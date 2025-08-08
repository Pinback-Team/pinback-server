package com.pinback.shared.constant;

import lombok.Getter;

@Getter
public enum ExceptionCode {

	//400
	INVALID_INPUT_VALUE("c40000", "잘못된 요청입니다."),
	CATEGORY_LIMIT_OVER("c40001", "카테고리는 최대 10개까지 생성할 수 있습니다."),
	TEXT_LENGTH_OVER("c40002", "글자 수 제한을 초과하였습니다."),
	CATEGORY_NAME_INVALID("c40003", "카테고리 이름은 공백을 포함할 수 없습니다."),

	//401
	INVALID_TOKEN("c40101", "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN("c40102", "만료된 토큰입니다."),
	EMPTY_TOKEN("c40103", "토큰이 비어있습니다."),

	//403
	NOT_ARTICLE_OWNER("c40301", "아티클에 대한 소유권이 없습니다."),
	NOT_CATEGORY_OWNER("c40302", "카테고리에 대한 소유권이 없습니다."),

	//404
	NOT_FOUND("c40400", "리소스가 존재하지 않습니다."),
	USER_NOT_FOUND("c40401", "사용자가 존재하지 않습니다."),
	CATEGORY_NOT_FOUND("c40402", "카테고리가 존재하지 않습니다."),
	ARTICLE_NOT_FOUND("c40403", "아티클이 존재하지 않습니다."),
	SUBSCRIPTION_NOT_FOUND("c40404", "구독정보가 존재하지 않습니다."),

	//405
	METHOD_NOT_ALLOWED("c40500", "잘못된 HTTP method 요청입니다."),

	//409
	DUPLICATE("c40900", "이미 존재하는 리소스입니다."),
	USER_ALREADY_EXIST("c40901", "이미 존재하는 사용자입니다."),
	ARTICLE_ALREADY_EXIST("c40902", "이미 저장한 url 입니다."),
	CATEGORY_ALREADY_EXIST("c40903", "이미 존재하는 카테고리입니다."),

	//500
	INTERNAL_SERVER_ERROR("s50000", "서버 내부 오류가 발생했습니다.");

	private final String code;
	private final String message;

	ExceptionCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
