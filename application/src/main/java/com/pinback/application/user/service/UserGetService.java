package com.pinback.application.user.service;

import java.util.Optional;
import java.util.UUID;

import com.pinback.domain.user.entity.User;

public interface UserGetService {
	Optional<User> findByEmail(String email);

	User findById(UUID id);
}
