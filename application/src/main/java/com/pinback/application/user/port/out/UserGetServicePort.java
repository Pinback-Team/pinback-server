package com.pinback.application.user.port.out;

import java.util.UUID;

import com.pinback.domain.user.entity.User;

import reactor.core.publisher.Mono;

public interface UserGetServicePort {
	User findByEmail(String email);

	User findById(UUID id);

	Mono<User> findUserByEmail(String email);
}
