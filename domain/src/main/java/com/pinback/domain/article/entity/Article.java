package com.pinback.domain.article.entity;

import com.pinback.domain.common.BaseEntity;
import com.pinback.shared.exception.TextLengthOverException;
import com.pinback.shared.util.TextUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "article_migration", uniqueConstraints = 
	@UniqueConstraint(columnNames = {"user_id", "url"}))
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;
	
	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "memo")
	private String memo;
	
	@Column(name = "user_id")
	private UUID userId;
	
	@Column(name = "category_id")
	private Long categoryId;
	
	@Column(name = "remind_at")
	private LocalDateTime remindAt;
	
	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	public static Article create(String url, String memo, UUID userId, Long categoryId, LocalDateTime remindAt) {
		validateMemo(memo);

		return Article.builder()
			.url(url)
			.memo(memo)
			.userId(userId)
			.categoryId(categoryId)
			.isRead(false)
			.remindAt(remindAt)
			.build();
	}

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

	public void markAsUnread() {
		this.isRead = false;
	}

	public void update(String memo, Long categoryId, LocalDateTime remindAt) {
		validateMemo(memo);
		this.memo = memo;
		this.categoryId = categoryId;
		this.remindAt = remindAt;
	}

	public boolean isOwnedBy(UUID userId) {
		return this.userId.equals(userId);
	}

	public boolean hasReminder() {
		return this.remindAt != null;
	}

	public boolean isReminderDue(LocalDateTime now) {
		return hasReminder() && this.remindAt.isBefore(now);
	}
}
