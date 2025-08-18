package com.pinback.domain.category.entity;

import com.pinback.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "category_migration")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;
	
	@Column(name = "category_name", nullable = false)
	private String name;
	
	@Column(name = "user_id")
	private UUID userId;

	public static Category create(String name, UUID userId) {
		return Category.builder()
			.name(name)
			.userId(userId)
			.build();
	}

	public void updateName(String name) {
		this.name = name;
	}

	public boolean isOwnedBy(UUID userId) {
		return this.userId.equals(userId);
	}
}
