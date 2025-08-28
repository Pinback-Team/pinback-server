package com.pinback.api.user.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinback.application.user.dto.command.UpdateUserCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사용자 정보 수정 요청")
public record UpdateUserRequest(
	@Schema(description = "이메일", example = "user@example.com")
	@NotBlank(message = "이메일은 필수입니다")
	@Email(message = "올바른 이메일 형식이어야 합니다")
	String email,
	
	@Schema(description = "기본 리마인더 시간", example = "08:30", pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm")
	@NotNull(message = "기본 리마인더 시간은 필수입니다")
	LocalTime remindDefault
) {
	public UpdateUserCommand toCommand(java.util.UUID userId) {
		return new UpdateUserCommand(userId, email, remindDefault);
	}
}
