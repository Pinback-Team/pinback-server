package com.pinback.infrastructure.user.service;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.port.out.UserUpdateServicePort;
import com.pinback.domain.common.enums.Job;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUpdateService implements UserUpdateServicePort {
	private final UserRepository userRepository;

	@Override
	public void updateRemindDefault(UUID userId, LocalTime remindDefault) {
		userRepository.updateRemindDefault(userId, remindDefault);
	}

	@Override
	public Mono<User> updateUser(User user) {
		return Mono.fromCallable(() -> {
			return userRepository.save(user);
		}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public void updateProfileImage(UUID userId, String imageProfile) {
		userRepository.updateProfileImage(userId, imageProfile);
	}

	@Override
	public void updateJob(UUID userId, Job job) {
		userRepository.updateJob(userId, job);
	}
}
