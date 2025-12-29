package com.pinback.infrastructure.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserNotFoundException;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGetService implements UserGetServicePort {

	private final UserRepository userRepository;

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	}

	@Override
	public User findById(UUID id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	@Override
	public Mono<User> findUserByEmail(String email) {
		return Mono.justOrEmpty(userRepository.findByEmail(email));
	}
}
