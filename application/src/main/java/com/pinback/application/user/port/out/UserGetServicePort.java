package com.pinback.application.user.port.out;

import java.util.UUID;

import com.pinback.domain.user.entity.User;

public interface UserGetServicePort {
	User findByEmail(String email);

	User findById(UUID id);
}
