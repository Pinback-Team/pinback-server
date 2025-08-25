package com.pinback.pinback_server.infra.redis.dto.response;

import lombok.Builder;

@Builder
public record AcornCollectResponse(
	int finalAcornCount,
	boolean isCollected
) {
}
