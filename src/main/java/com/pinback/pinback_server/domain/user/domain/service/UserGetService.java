package com.pinback.pinback_server.domain.user.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;
import com.pinback.pinback_server.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserGetService {
	private final UserRepository userRepository;

	public User getUser(UUID userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
