package com.pinback.api.category.dto.request;

import com.pinback.application.category.dto.command.UpdateCategoryCommandV3;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 수정 요청 V3")
public record UpdateCategoryRequestV3(
	@Schema(description = "카테고리 이름", example = "수정된 개발")
	@NotBlank(message = "카테고리 이름은 필수입니다")
	@Size(max = 50, message = "카테고리 이름은 50자 이하로 입력해주세요")
	String categoryName,
	@Schema(description = "카테고리 공개여부", example = "false")
	@NotNull(message = "공개 여부는 필수 항목입니다(true/false)")
	Boolean isPublic
) {
	public UpdateCategoryCommandV3 toCommand() {
		return new UpdateCategoryCommandV3(categoryName, isPublic);
	}
}
