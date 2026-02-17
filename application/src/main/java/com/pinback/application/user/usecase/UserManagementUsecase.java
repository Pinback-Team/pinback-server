package com.pinback.application.user.usecase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.application.config.ProfileImageConfig;
import com.pinback.application.user.dto.command.UpdateUserJobCommand;
import com.pinback.application.user.dto.response.UserGoogleProfileResponse;
import com.pinback.application.user.dto.response.UserInfoResponse;
import com.pinback.application.user.dto.response.UserJobInfoResponse;
import com.pinback.application.user.dto.response.UserProfileInfoResponse;
import com.pinback.application.user.dto.response.UserRemindInfoResponse;
import com.pinback.application.user.port.in.UserManagementPort;
import com.pinback.application.user.port.out.AcornServicePort;
import com.pinback.application.user.port.out.UserGetServicePort;
import com.pinback.domain.common.enums.Job;
import com.pinback.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementUsecase implements UserManagementPort {
	private final AcornServicePort acornService;
	private final ProfileImageConfig profileImageConfig;
	private final UserGetServicePort userGetServicePort;

	@Override
	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(User user, LocalDateTime now) {
		LocalTime userRemindDefault = user.getRemindDefault();
		LocalDateTime remindDateTime = getRemindDateTime(now, userRemindDefault);

		int finalAcornCount = acornService.getCurrentAcorns(user.getId());

		return UserInfoResponse.of(finalAcornCount, remindDateTime);
	}

	@Override
	@Transactional(readOnly = true)
	public UserRemindInfoResponse getUserRemindInfo(User user, LocalDateTime now) {
		LocalTime userRemindTime = user.getRemindDefault();
		LocalDate userRemindDate = now.toLocalDate().plusDays(1L);

		return UserRemindInfoResponse.of(userRemindDate, userRemindTime);
	}

	@Override
	@Transactional(readOnly = true)
	public UserProfileInfoResponse getUserProfileInfo(User user) {
		String name = user.getUsername();
		String email = user.getEmail();
		LocalTime userRemindDefault = user.getRemindDefault();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a HH:mm").withLocale(Locale.US);
		String remindAt = userRemindDefault.format(formatter);
		String profileImage = profileImageConfig.getImageUrl(user.getProfileImage());

		return UserProfileInfoResponse.of(name, email, remindAt, profileImage);
	}

	@Override
	@Transactional(readOnly = true)
	public UserGoogleProfileResponse getUserGoogleProfile(User user) {
		String googleProfile = user.getGoogleProfileImage();
		return UserGoogleProfileResponse.of(googleProfile);
	}

	@Override
	@Transactional
	public UserJobInfoResponse updateUserJobInfo(User user, UpdateUserJobCommand command) {
		User getUser = userGetServicePort.findById(user.getId());

		Job job = Job.from(command.job());
		getUser.updateJob(job);
		log.info("user: {}, job: {}, user.job: {}", user.getId(), job, getUser.getJob());

		String updatedJob = getUser.getJob().getValue();
		return UserJobInfoResponse.of(updatedJob);
	}

	private LocalDateTime getRemindDateTime(LocalDateTime now, LocalTime remindDefault) {
		LocalDateTime remindDate = now.plusDays(1L);

		return LocalDateTime.of(remindDate.getYear(), remindDate.getMonth(),
			remindDate.getDayOfMonth(),
			remindDefault.getHour(), remindDefault.getMinute());
	}

}
