package com.pinback.domain.category.entity;

import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.category.exception.CategoryNameLengthOverException;
import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.util.TextUtil;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_category_user_color",
		columnNames = {"user_id", "color"}
	)
)
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

	@Column(name = "color", nullable = false)
	@Enumerated(EnumType.STRING)
	private CategoryColor color;

	@Column(name = "is_public", nullable = false)
	private Boolean isPublic;

	public static Category create(String name, User user, CategoryColor color) {
		validateName(name);
		return Category.builder()
			.name(name)
			.user(user)
			.color(color)
			.build();
	}

	public static Category createWithIsPublic(String name, User user, CategoryColor color, Boolean isPublic) {
		validateName(name);
		return Category.builder()
			.name(name)
			.user(user)
			.color(color)
			.isPublic(isPublic)
			.build();
	}

	private static void validateName(String name) {
		int characterCount = TextUtil.countGraphemeClusters(name);
		if (characterCount > 10) {
			throw new CategoryNameLengthOverException();
		}
	}

	public void updateName(String name) {
		validateName(name);
		this.name = name;
	}

	public boolean isOwnedBy(User user) {
		return this.user.equals(user);
	}

	public void updateIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
}
