package com.pinback.application.notification.usecase.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.notification.port.in.SavePushSubscriptionPort;
import com.pinback.application.notification.port.out.PushSubscriptionSaveServicePort;
import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SavePushSubscriptionUsecase implements SavePushSubscriptionPort {

	private final PushSubscriptionSaveServicePort pushSubscriptionSaveService;

	@Override
	public PushSubscription savePushSubscription(User user, String token) {
		PushSubscription pushSubscription = pushSubscriptionSaveService.save(PushSubscription.create(user, token));
		return pushSubscription;
	}
}
