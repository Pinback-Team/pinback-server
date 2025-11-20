package com.pinback.application.user.port.out;

import java.time.LocalTime;
import java.util.UUID;

public interface UserUpdateServicePort {
	void updateRemindDefault(UUID userId, LocalTime remindDefault);
}
