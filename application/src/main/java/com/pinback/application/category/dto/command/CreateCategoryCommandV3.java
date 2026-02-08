package com.pinback.application.category.dto.command;

public record CreateCategoryCommandV3(
	String categoryName,
	Boolean isPublic
) {
}
