package com.pinback.infrastructure.redis.dto.response;

import lombok.Builder;

@Builder
public record AcornCollectResponse(
	int finalAcornCount,
	boolean isCollected
) {
}
