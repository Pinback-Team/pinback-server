package com.pinback.domain.article.entity;

import java.time.LocalDateTime;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.common.BaseEntity;
import com.pinback.domain.user.entity.User;
import com.pinback.shared.exception.TextLengthOverException;
import com.pinback.shared.util.TextUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "article", uniqueConstraints =
@UniqueConstraint(
	columnNames = {"user_id", "url"}
))
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;

	@Column(name = "url", length = 700, nullable = false)
	private String url;

	@Column(name = "memo")
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "remind_at")
	private LocalDateTime remindAt;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	public static Article create(String url, String memo, User user, Category category, LocalDateTime remindAt) {
		validateMemo(memo);

		return Article.builder()
			.url(url)
			.memo(memo)
			.user(user)
			.category(category)
			.isRead(false)
			.remindAt(remindAt)
			.build();
	}

	// 아티클 자체의 비즈니스 로직을 보호하기 위한 유효성 검사
	private static void validateMemo(String memo) {
		if (memo != null && TextUtil.countGraphemeClusters(memo) > 500) {
			throw new TextLengthOverException();
		}
	}

	public boolean isRead() {
		return isRead;
	}

	public void markAsRead() {
		this.isRead = true;
	}

	public void update(String memo, Category category, LocalDateTime remindAt) {
		validateMemo(memo);
		this.memo = memo;
		this.category = category;
		this.remindAt = remindAt;
	}

	public boolean isOwnedBy(User user) {
		return this.user.equals(user);
	}

	public boolean hasReminder() {
		return this.remindAt != null;
	}

	public boolean isReminderDue(LocalDateTime now) {
		return hasReminder() && this.remindAt.isBefore(now);
	}
}
