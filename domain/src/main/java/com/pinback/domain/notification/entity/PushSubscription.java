package com.pinback.domain.notification.entity;

import java.util.UUID;

import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.notification.exception.EmptyFcmTokenException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "push_subscription_migration")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PushSubscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "push_subscription_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "token", nullable = false, unique = true)
	private String token;

	public static PushSubscription create(UUID userId, String token) {
		validateToken(token);
		return PushSubscription.builder()
			.userId(userId)
			.token(token)
			.build();
	}

	private static void validateToken(String token) {
		if (token == null || token.trim().isEmpty()) {
			throw new EmptyFcmTokenException();
		}
	}

	public boolean isOwnedBy(UUID userId) {
		return this.userId.equals(userId);
	}
}
