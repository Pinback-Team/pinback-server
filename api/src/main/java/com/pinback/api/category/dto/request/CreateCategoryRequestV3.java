package com.pinback.api.category.dto.request;

import com.pinback.application.category.dto.command.CreateCategoryCommandV3;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 생성 요청 V3")
public record CreateCategoryRequestV3(
	@Schema(description = "카테고리 이름", example = "개발")
	@NotBlank(message = "카테고리 이름은 필수입니다")
	@Size(max = 50, message = "카테고리 이름은 50자 이하로 입력해주세요")
	String categoryName,
	@Schema(description = "카테고리 공개여부", example = "true")
	Boolean isPublic
) {
	public CreateCategoryCommandV3 toCommand() {
		return new CreateCategoryCommandV3(categoryName, isPublic);
	}

}
