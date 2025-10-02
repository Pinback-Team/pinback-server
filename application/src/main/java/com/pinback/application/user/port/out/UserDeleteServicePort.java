package com.pinback.application.user.port.out;

import com.pinback.domain.user.entity.User;

public interface UserDeleteServicePort {
	void delete(User user);
}
