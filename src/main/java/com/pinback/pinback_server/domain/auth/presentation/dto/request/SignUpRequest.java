package com.pinback.pinback_server.domain.auth.presentation.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinback.pinback_server.domain.auth.application.command.SignUpCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
	@NotBlank(message = "이메일은 비어있을 수 없습니다.")
	String email,

	@Schema(description = "기본 알림 시간", example = "08:30:00", pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss")
	@NotNull(message = "리마인드 시간은 비어있을 수 없습니다.")
	LocalTime remindDefault
) {
	public SignUpCommand toCommand() {
		return SignUpCommand.of(email, remindDefault);
	}
}
