package com.pinback.infrastructure.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.port.out.UserSaveServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSaveService implements UserSaveServicePort {

	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Mono<User> saveUser(User user) {
		return Mono.fromCallable(() -> {
				return userRepository.save(user);
			})
			.subscribeOn(Schedulers.boundedElastic());
	}
}
