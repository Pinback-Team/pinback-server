package com.pinback.application.notification.port.out;

import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

public interface PushSubscriptionGetServicePort {

	PushSubscription findByUser(User user);
}
