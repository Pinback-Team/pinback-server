package com.pinback.application.user.port.in;

import java.time.LocalDateTime;

import com.pinback.application.user.dto.response.UserInfoResponse;
import com.pinback.application.user.dto.response.UserRemindInfoResponse;
import com.pinback.domain.user.entity.User;

public interface UserManagementPort {

	UserInfoResponse getUserInfo(User user, LocalDateTime now);

	UserRemindInfoResponse getUserRemindInfo(User user, LocalDateTime now);
}
