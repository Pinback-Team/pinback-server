package com.pinback.application.notification.port.out;

import com.pinback.domain.user.entity.User;

public interface PushSubscriptionDeleteServicePort {
	void deleteByUser(User user);
}
