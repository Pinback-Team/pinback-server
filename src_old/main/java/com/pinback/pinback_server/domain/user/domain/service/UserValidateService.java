package com.pinback.pinback_server.domain.user.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserValidateService {
	private final UserRepository userRepository;

	public boolean checkDuplicate(String email) {
		return userRepository.existsUserByEmail(email);
	}
}
