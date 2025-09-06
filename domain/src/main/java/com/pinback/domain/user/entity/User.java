package com.pinback.domain.user.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.pinback.domain.common.BaseEntity;

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
@Table(name = "users")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "remind_default", nullable = false)
	private LocalTime remindDefault;

	@Column(name = "acorn_count", nullable = false)
	private Long acornCount;

	public static User create(String email, LocalTime remindDefault) {
		return User.builder()
			.email(email)
			.remindDefault(remindDefault)
			.acornCount(0L)
			.build();
	}

	public void increaseAcornCount(Long count) {
		this.acornCount += count;
	}

	public void updateRemindDefault(LocalTime newRemindDefault) {
		this.remindDefault = newRemindDefault;
	}

	public boolean isNewUser(LocalDateTime now) {
		return getCreatedAt().isAfter(now.minusDays(3));
	}
}
