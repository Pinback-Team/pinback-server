package com.pinback.application.notification.port.in;

import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

public interface GetPushSubscriptionPort {
	PushSubscription findPushSubscription(User user);
}
