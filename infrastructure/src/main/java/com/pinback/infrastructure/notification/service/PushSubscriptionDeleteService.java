package com.pinback.infrastructure.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.out.PushSubscriptionDeleteServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.notification.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PushSubscriptionDeleteService implements PushSubscriptionDeleteServicePort {
	private final PushSubscriptionRepository pushSubscriptionRepository;

	@Override
	public void deleteByUser(User user) {
		pushSubscriptionRepository.deleteByUser(user);
	}
}
