package com.pinback.domain.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.user.entity.User;
import com.pinback.domain.user.exception.UserNotFoundException;
import com.pinback.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGetService {

	private final UserRepository userRepository;

	public User getUser(UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);
	}

}
