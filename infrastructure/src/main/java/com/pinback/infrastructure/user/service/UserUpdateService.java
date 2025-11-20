package com.pinback.infrastructure.user.service;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.port.out.UserUpdateServicePort;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUpdateService implements UserUpdateServicePort {
	private final UserRepository userRepository;

	@Override
	public void updateRemindDefault(UUID userId, LocalTime remindDefault) {
		userRepository.updateRemindDefault(userId, remindDefault);
	}
}
