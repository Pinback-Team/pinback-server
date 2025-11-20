package com.pinback.application.user.port.out;

import com.pinback.domain.user.entity.User;

import reactor.core.publisher.Mono;

public interface UserSaveServicePort {
	User save(User user);

	Mono<User> saveUser(User user);
}
