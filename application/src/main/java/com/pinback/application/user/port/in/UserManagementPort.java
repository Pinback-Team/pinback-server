package com.pinback.application.user.port.in;

import java.time.LocalDateTime;

import com.pinback.application.user.dto.command.UpdateUserJobCommand;
import com.pinback.application.user.dto.response.UserGoogleProfileResponse;
import com.pinback.application.user.dto.response.UserInfoResponse;
import com.pinback.application.user.dto.response.UserJobInfoResponse;
import com.pinback.application.user.dto.response.UserProfileInfoResponse;
import com.pinback.application.user.dto.response.UserRemindInfoResponse;
import com.pinback.domain.user.entity.User;

public interface UserManagementPort {

	UserInfoResponse getUserInfo(User user, LocalDateTime now);

	UserRemindInfoResponse getUserRemindInfo(User user, LocalDateTime now);

	UserProfileInfoResponse getUserProfileInfo(User user);

	UserGoogleProfileResponse getUserGoogleProfile(User user);

	UserJobInfoResponse updateUserJobInfo(User user, UpdateUserJobCommand command);
}
