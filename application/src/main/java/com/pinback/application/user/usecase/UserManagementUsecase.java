package com.pinback.application.user.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.user.dto.response.UserInfoResponse;
import com.pinback.application.user.dto.response.UserRemindInfoResponse;
import com.pinback.application.user.port.in.UserManagementPort;
import com.pinback.application.user.port.out.AcornServicePort;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserManagementUsecase implements UserManagementPort {
	private final AcornServicePort acornService;

	@Override
	public UserInfoResponse getUserInfo(User user, LocalDateTime now) {
		LocalTime userRemindDefault = user.getRemindDefault();
		LocalDateTime remindDateTime = getRemindDateTime(now, userRemindDefault);

		int finalAcornCount = acornService.getCurrentAcorns(user.getId());

		return UserInfoResponse.of(finalAcornCount, remindDateTime);
	}

	@Override
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
}
