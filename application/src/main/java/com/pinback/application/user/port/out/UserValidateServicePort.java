package com.pinback.application.user.port.out;

import com.pinback.domain.user.entity.User;

public interface UserValidateServicePort {
	void validateDuplicate(String email);

	boolean validateLogin(String email, String password);

	User authenticate(String email, String password);
}
