package com.pinback.infrastructure.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.port.out.UserSaveServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUpdateService implements UserSaveServicePort {

	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
