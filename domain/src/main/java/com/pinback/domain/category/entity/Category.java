package com.pinback.domain.category.entity;

import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "category")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "color")
	@Enumerated(EnumType.STRING)
	private CategoryColor color;

	public static Category create(String name, User user, CategoryColor color) {
		return Category.builder()
			.name(name)
			.user(user)
			.color(color)
			.build();
	}

	public void updateName(String name) {
		this.name = name;
	}

	public boolean isOwnedBy(User user) {
		return this.user.equals(user);
	}
}
