package com.pinback.application.notification.usecase.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.in.GetPushSubscriptionPort;
import com.pinback.application.notification.port.out.PushSubscriptionGetServicePort;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPushSubscriptionUsecase implements GetPushSubscriptionPort {

	private final PushSubscriptionGetServicePort pushSubscriptionGetService;

	@Override
	public PushSubscription findPushSubscription(User user) {
		PushSubscription pushSubscription = pushSubscriptionGetService.findByUser(user);
		return pushSubscription;
	}
}
