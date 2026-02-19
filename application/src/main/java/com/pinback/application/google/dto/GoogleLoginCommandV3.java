package com.pinback.application.google.dto;

public record GoogleLoginCommandV3(
	String code,
	String uri
) {
}
