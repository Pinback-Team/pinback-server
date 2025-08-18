package com.pinback.domain.user.entity;

import com.pinback.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users_migration")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

	public void decreaseAcornCount(Long count) {
		if (this.acornCount >= count) {
			this.acornCount -= count;
		}
	}

	public boolean canRemindAt(LocalTime time) {
		return this.remindDefault != null;
	}
}
