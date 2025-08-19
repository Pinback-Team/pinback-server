package com.pinback.application.notification.service;

import com.pinback.domain.notification.entity.PushSubscription;

public interface PushSubscriptionSaveService {
	PushSubscription save(PushSubscription pushSubscription);
}