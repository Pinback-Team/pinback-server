package com.pinback.pinback_server.domain.article.domain.entity;

import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.global.entity.BaseEntity;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article")
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "memo")
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	public static Article create(String url, String memo, User user, Category category) {
		return Article.builder()
			.url(url)
			.memo(memo)
			.user(user)
			.category(category)
			.isRead(false)
			.build();
	}
}
