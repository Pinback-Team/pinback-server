package com.pinback.application.category.dto.command;

public record UpdateCategoryCommandV3(
	String categoryName,
	Boolean isPublic
) {
}
