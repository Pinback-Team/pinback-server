package com.pinback.infrastructure.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.common.exception.UserDuplicateException;
import com.pinback.application.user.port.out.UserValidateServicePort;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserValidateService implements UserValidateServicePort {

	private final UserRepository userRepository;

	@Override
	public void validateDuplicateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new UserDuplicateException();
		}
	}
}
