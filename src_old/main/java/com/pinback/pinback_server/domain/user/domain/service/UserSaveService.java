package com.pinback.pinback_server.domain.user.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSaveService {
	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}
}
