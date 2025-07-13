package com.pinback.pinback_server.infra.redis.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationData {
	private Long articleId;
	private String userId;
	private String fcmToken;
	private String url;
	private LocalDateTime scheduledTime;
	private LocalDateTime createdAt;
}
