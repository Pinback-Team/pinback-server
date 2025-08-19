package com.pinback.application.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

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
