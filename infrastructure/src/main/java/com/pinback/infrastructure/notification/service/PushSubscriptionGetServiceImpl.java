package com.pinback.infrastructure.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.out.PushSubscriptionGetServicePort;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.notification.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushSubscriptionGetServiceImpl implements PushSubscriptionGetServicePort {

	private final PushSubscriptionRepository pushSubscriptionRepository;

	@Override
	public PushSubscription findByUser(User user) {
		return pushSubscriptionRepository.findByUser(user)
			.orElse(null);
	}
}
