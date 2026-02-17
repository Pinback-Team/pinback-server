package com.pinback.domain.user.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.common.enums.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	private static final LocalTime TEMP_REMIND_DEFAULT_MARKER = null;
	private static final Long DEFAULT_ACORN_COUNT = 0L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "remind_default", nullable = true)
	private LocalTime remindDefault;

	@Column(name = "acorn_count", nullable = false)
	private Long acornCount;

	@Column(name = "google_profile_image", unique = true)
	private String googleProfileImage;

	@Column(name = "user_name")
	private String username;

	@Column(name = "profile_image")
	private String profileImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "job")
	private Job job;

	public static User create(String email, LocalTime remindDefault) {
		return User.builder()
			.email(email)
			.remindDefault(remindDefault)
			.acornCount(DEFAULT_ACORN_COUNT)
			.build();
	}

	public static User createTempUser(String email, String name) {
		return User.builder()
			.email(email)
			.remindDefault(TEMP_REMIND_DEFAULT_MARKER)
			.acornCount(DEFAULT_ACORN_COUNT)
			.username(name)
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

	public void updateGoogleProfileImage(String googleProfileImage) {
		this.googleProfileImage = googleProfileImage;
	}

	public void updateName(String name) {
		this.username = name;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updateJob(Job job) {
		this.job = job;
	}

	public boolean hasJob() {
		return this.job != null;
	}
}
