package com.pinback.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.user.exception.UserDuplicateException;
import com.pinback.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserValidateService {

	private final UserRepository userRepository;

	private boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void validateEmailUniqueness(String email) {
		if (existsByEmail(email)) {
			throw new UserDuplicateException();
		}
	}
}
