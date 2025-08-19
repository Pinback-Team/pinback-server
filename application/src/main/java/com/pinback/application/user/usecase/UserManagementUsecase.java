package com.pinback.application.user.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.service.UserGetService;
import com.pinback.domain.user.entity.User;
import com.pinback.application.user.service.AcornService;

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
		LocalTime userRemindDefault = user.getRemindDefault();
		LocalDateTime remindDateTime = getRemindDateTime(now, userRemindDefault);

		int finalAcornCount = acornService.getCurrentAcorns(user.getId());

		return UserInfoResponse.of(finalAcornCount, remindDateTime);
	}

	@Transactional(readOnly = true)
	public UserRemindInfoResponse getUserRemindInfo(User user, LocalDateTime now) {
		LocalTime userRemindTime = user.getRemindDefault();
		LocalDate userRemindDate = now.toLocalDate().plusDays(1L);

		return UserRemindInfoResponse.of(userRemindDate, userRemindTime);
	}

	private LocalDateTime getRemindDateTime(LocalDateTime now, LocalTime remindDefault) {
		LocalDateTime remindDate = now.plusDays(1L);

		return LocalDateTime.of(remindDate.getYear(), remindDate.getMonth(),
			remindDate.getDayOfMonth(),
			remindDefault.getHour(), remindDefault.getMinute());
	}

	public static class UserInfoResponse {
		public static UserInfoResponse of(int acornCount, LocalDateTime remindDateTime) {
			// Implementation
			return null;
		}
	}

	public static class UserRemindInfoResponse {
		public static UserRemindInfoResponse of(LocalDate remindDate, LocalTime remindTime) {
			// Implementation
			return null;
		}
	}
}