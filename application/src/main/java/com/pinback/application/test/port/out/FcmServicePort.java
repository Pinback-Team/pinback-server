package com.pinback.application.test.port.out;

public interface FcmServicePort {
	void sendNotification(String token, String message);
}
