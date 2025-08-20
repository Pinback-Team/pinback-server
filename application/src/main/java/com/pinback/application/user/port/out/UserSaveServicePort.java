package com.pinback.application.user.port.out;

import com.pinback.domain.user.entity.User;

public interface UserSaveServicePort {
	User save(User user);
}
