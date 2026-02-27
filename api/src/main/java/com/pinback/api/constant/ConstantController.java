package com.pinback.api.constant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.constant.dto.response.JobsResponse;
import com.pinback.application.constant.port.in.ConstantManagementPort;
import com.pinback.shared.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/enums")
public class ConstantController {
	private final ConstantManagementPort constantManagementPort;

	@GetMapping("/jobs")
	public ResponseDto<JobsResponse> getJobs() {
		JobsResponse response = constantManagementPort.getJobs();
		return ResponseDto.ok(response);
	}

}
