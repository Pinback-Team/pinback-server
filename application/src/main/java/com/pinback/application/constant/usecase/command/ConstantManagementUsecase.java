package com.pinback.application.constant.usecase.command;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.config.JobImageConfig;
import com.pinback.application.constant.dto.response.JobResponse;
import com.pinback.application.constant.dto.response.JobsResponse;
import com.pinback.application.constant.port.in.ConstantManagementPort;
import com.pinback.domain.common.enums.Job;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstantManagementUsecase implements ConstantManagementPort {
	private final JobImageConfig jobImageConfig;

	@Override
	@Transactional(readOnly = true)
	public JobsResponse getJobs() {
		List<JobResponse> jobResponses = Arrays.stream(Job.values())
			.map(job -> new JobResponse(
				jobImageConfig.getImageUrl(job.getKey()),
				job.getValue()
			))
			.toList();

		return JobsResponse.of(jobResponses);
	}

}
