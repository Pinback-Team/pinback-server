package com.pinback.pinback_server.domain.user.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserGetService;
import com.pinback.pinback_server.domain.user.presentation.dto.response.UserInfoResponse;
import com.pinback.pinback_server.domain.user.presentation.dto.response.UserRemindInfoResponse;
import com.pinback.pinback_server.infra.redis.AcornService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementUsecase {
	private final UserGetService userGetService;
	private final AcornService acornService;

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(User user, LocalDateTime now) {
		User getUser = userGetService.getUser(user.getId());

		LocalTime userRemindDefault = getUser.getRemindDefault();
		LocalDateTime remindDateTime = getRemindDateTime(now, userRemindDefault);

		int finalAcornCount = acornService.getCurrentAcorns(getUser.getId());

		return UserInfoResponse.of(finalAcornCount, remindDateTime);
	}

	@Transactional(readOnly = true)
	public UserRemindInfoResponse getUserRemindInfo(User user, LocalDateTime now) {
		User getUser = userGetService.getUser(user.getId());
		LocalTime userRemindTime = getUser.getRemindDefault();
		LocalDate userRemindDate = now.toLocalDate().plusDays(1L);

		return UserRemindInfoResponse.of(userRemindDate, userRemindTime);
	}

	private LocalDateTime getRemindDateTime(LocalDateTime now, LocalTime remindDefault) {
		LocalDateTime remindDate = now.plusDays(1L);

		return LocalDateTime.of(remindDate.getYear(), remindDate.getMonth(),
			remindDate.getDayOfMonth(),
			remindDefault.getHour(), remindDefault.getMinute());
	}
}
