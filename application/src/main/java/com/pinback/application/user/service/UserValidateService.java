package com.pinback.application.user.service;

import com.pinback.domain.user.entity.User;

public interface UserValidateService {
	void validateDuplicate(String email);

	boolean validateLogin(String email, String password);

	User authenticate(String email, String password);
}
