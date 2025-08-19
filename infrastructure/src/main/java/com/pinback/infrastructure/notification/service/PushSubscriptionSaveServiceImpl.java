package com.pinback.infrastructure.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.service.PushSubscriptionSaveService;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.infrastructure.notification.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PushSubscriptionSaveServiceImpl implements PushSubscriptionSaveService {

	private final PushSubscriptionRepository pushSubscriptionRepository;

	@Override
	public PushSubscription save(PushSubscription pushSubscription) {
		return pushSubscriptionRepository.save(pushSubscription);
	}
}