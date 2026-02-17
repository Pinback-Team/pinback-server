package com.pinback.application.user.port.out;

import java.time.LocalTime;
import java.util.UUID;

import com.pinback.domain.common.enums.Job;
import com.pinback.domain.user.entity.User;

import reactor.core.publisher.Mono;

public interface UserUpdateServicePort {
	void updateRemindDefault(UUID userId, LocalTime remindDefault);

	Mono<User> updateUser(User user);

	void updateProfileImage(UUID userId, String imageProfile);

	void updateJob(UUID userId, Job job);
}
