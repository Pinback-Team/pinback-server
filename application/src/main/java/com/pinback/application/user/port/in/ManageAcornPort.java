package com.pinback.application.user.port.in;

import java.util.UUID;

import com.pinback.application.article.dto.AcornCollectResult;
import com.pinback.domain.user.entity.User;

public interface ManageAcornPort {
	int getCurrentAcorns(UUID userId);
	AcornCollectResult tryCollectAcorns(User user);
}