package com.pinback.pinback_server.domain.notification.domain.service;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;
import com.pinback.pinback_server.domain.notification.domain.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSaveService {
	private final PushSubscriptionRepository repository;

	public void save(PushSubscription pushSubscription) {
		repository.save(pushSubscription);
	}
}
