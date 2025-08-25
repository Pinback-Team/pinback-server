package com.pinback.pinback_server.domain.category.presentation.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryAllExtensionResponse(
	String recentSaved,
	List<CategoryExtensionResponse> categories
) {
	public static CategoryAllExtensionResponse of(String recentSaved, List<CategoryExtensionResponse> categories) {
		return new CategoryAllExtensionResponse(recentSaved, categories);
	}
}
