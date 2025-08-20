package com.pinback.infrastructure.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserDuplicateException;
import com.pinback.application.common.exception.UserNotFoundException;
import com.pinback.application.user.port.out.UserValidateServicePort;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserValidateService implements UserValidateServicePort {

	private final UserRepository userRepository;

	@Override
	public void validateDuplicate(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new UserDuplicateException();
		}
	}

	@Override
	public boolean validateLogin(String email, String password) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User authenticate(String email, String password) {
		return userRepository.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);
	}
}
