package com.pinback.application.notification.dto.command;

import java.util.UUID;

public record SavePushSubscriptionCommand(
	String endpoint,
	String auth,
	String p256dh,
	UUID userId
) {
}