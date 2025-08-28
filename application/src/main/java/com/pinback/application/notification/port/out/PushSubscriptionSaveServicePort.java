package com.pinback.application.notification.port.out;

import com.pinback.domain.notification.entity.PushSubscription;

public interface PushSubscriptionSaveServicePort {

	PushSubscription save(PushSubscription pushSubscription);
}
