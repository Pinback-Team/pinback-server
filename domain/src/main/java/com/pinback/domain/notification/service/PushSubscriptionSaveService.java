package com.pinback.domain.notification.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.notification.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PushSubscriptionSaveService {

	private final PushSubscriptionRepository pushSubscriptionRepository;

	public PushSubscription save(PushSubscription pushSubscription) {
		return pushSubscriptionRepository.save(pushSubscription);
	}

	public void deleteByUserId(UUID userId) {
		pushSubscriptionRepository.deleteByUserId(userId);
	}
}
