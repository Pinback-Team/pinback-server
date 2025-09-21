package com.pinback.infrastructure.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.port.out.UserDeleteServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeleteService implements UserDeleteServicePort {
	private final UserRepository userRepository;

	@Override
	public void delete(User user) {
		userRepository.deleteById(user.getId());
	}
}
