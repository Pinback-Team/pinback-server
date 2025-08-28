package com.pinback.application.user.port.out;

import java.util.UUID;

import com.pinback.application.article.dto.AcornCollectResult;
import com.pinback.domain.user.entity.User;

public interface AcornServicePort {
	int getCurrentAcorns(UUID userId);

	AcornCollectResult tryCollectAcorns(User user);

	void resetAcornsForTest(UUID userId);
}
