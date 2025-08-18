package com.pinback.domain.notification.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.notification.repository.PushSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushSubscriptionGetService {

	private final PushSubscriptionRepository pushSubscriptionRepository;

	public List<PushSubscription> findByUserId(UUID userId) {
		return pushSubscriptionRepository.findByUserId(userId);
	}

	public Optional<PushSubscription> findByToken(String token) {
		return pushSubscriptionRepository.findByToken(token);
	}

	public boolean existsByToken(String token) {
		return pushSubscriptionRepository.existsByToken(token);
	}
}
