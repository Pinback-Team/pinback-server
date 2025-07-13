package com.pinback.pinback_server.domain.notification.domain.service;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;
import com.pinback.pinback_server.domain.notification.domain.repository.PushSubscriptionRepository;
import com.pinback.pinback_server.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushSubscriptionGetService {
	private final PushSubscriptionRepository pushSubscriptionRepository;

	public PushSubscription find(User user) {
		return pushSubscriptionRepository.findPushSubscriptionByUser(user);
	}
}
