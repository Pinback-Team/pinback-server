package com.pinback.application.user.port.out;

public interface UserValidateServicePort {
	void validateDuplicateEmail(String email);
}
