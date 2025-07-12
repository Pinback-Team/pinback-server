package com.pinback.pinback_server.domain.user.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.service.UserGetService;
import com.pinback.pinback_server.domain.user.presentation.dto.response.UserInfoResponse;
import com.pinback.pinback_server.infra.redis.AcornService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementUsecase {
	private final UserGetService userGetService;
	private final AcornService acornService;

	public UserInfoResponse getUserInfo(User user) {
		User getUser = userGetService.getUser(user.getId());

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime remindDate = now.plusDays(1L);
		LocalDateTime remindDateTime = LocalDateTime.of(remindDate.getYear(), remindDate.getMonth(),
			remindDate.getDayOfMonth(),
			getUser.getRemindDefault().getHour(), getUser.getRemindDefault().getMinute());

		String formattedRemindTime = formatRemindDateTime(remindDateTime);

		int finalAcornCount = acornService.getCurrentAcorns(getUser.getId());

		return UserInfoResponse.of(finalAcornCount, formattedRemindTime);
	}

	private String formatRemindDateTime(LocalDateTime remindDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a HH시 mm분", Locale.KOREAN);
		return remindDateTime.format(formatter);
	}
}
