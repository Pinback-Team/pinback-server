package com.pinback.domain.notification.entity;

import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.notification.exception.EmptyFcmTokenException;
import com.pinback.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "push_subscriptio")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PushSubscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "push_subscription_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "token", nullable = false, unique = true)
	private String token;

	public static PushSubscription create(User user, String token) {
		validateToken(token);
		return PushSubscription.builder()
			.user(user)
			.token(token)
			.build();
	}

	private static void validateToken(String token) {
		if (token == null || token.trim().isEmpty()) {
			throw new EmptyFcmTokenException();
		}
	}

	public boolean isOwnedBy(User user) {
		return this.user.equals(user);
	}
}
