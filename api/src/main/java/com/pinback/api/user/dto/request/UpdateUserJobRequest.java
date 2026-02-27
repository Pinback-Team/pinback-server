package com.pinback.api.user.dto.request;

import com.pinback.application.user.dto.command.UpdateUserJobCommand;

public record UpdateUserJobRequest(
	String job
) {
	public UpdateUserJobCommand toCommand() {
		return new UpdateUserJobCommand(job);
	}
}
