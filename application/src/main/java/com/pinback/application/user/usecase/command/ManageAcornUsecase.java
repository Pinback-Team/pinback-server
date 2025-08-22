package com.pinback.application.user.usecase.command;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.article.dto.AcornCollectResult;
import com.pinback.application.user.port.in.ManageAcornPort;
import com.pinback.application.user.port.out.AcornServicePort;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageAcornUsecase implements ManageAcornPort {

	private final AcornServicePort acornServicePort;

	@Override
	public int getCurrentAcorns(UUID userId) {
		return acornServicePort.getCurrentAcorns(userId);
	}

	@Override
	public AcornCollectResult tryCollectAcorns(User user) {
		return acornServicePort.tryCollectAcorns(user);
	}
}