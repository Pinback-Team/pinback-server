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
		// 현재 프로젝트에서는 패스워드 없이 이메일만으로 인증하는 것으로 보임
		return userRepository.existsByEmail(email);
	}

	@Override
	public User authenticate(String email, String password) {
		// 이메일로 사용자 찾기 (패스워드 검증은 현재 구현되지 않음)
		return userRepository.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);
	}
}