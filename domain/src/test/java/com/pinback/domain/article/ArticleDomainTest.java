package com.pinback.domain.article;

import static com.pinback.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.fixture.TestFixture;

class ArticleDomainTest {

	@DisplayName("같은 사용자라면 Article 인스턴스는 동일한 것으로 판단한다.")
	@Test
	void testIsOwnedBy() {
		//given
		User user = TestFixture.user();
		Article article = TestFixture.article(user);

		//when
		boolean isOwnedBy = article.isOwnedBy(user);

		//then
		assertThat(isOwnedBy).isTrue();
	}

	@DisplayName("최초 생성된 Article 인스턴스는 읽지 않은 상태로 초기화된다.")
	@Test
	void testDefaultReadStatus() {
		//given
		User user = TestFixture.user();
		Category category = category(user);

		//when & then
		Article article = Article.create("test", "testmemo", user, category, LocalDateTime.of(2025, 7, 7, 12, 0, 0));
		assertThat(article.isRead()).isFalse();
	}

	@DisplayName("Article 생성시 메모의 길이가 300개 초과하면 예외가 발생한다.")
	@Test
	void testMaxLength() {
		//given
		User user = TestFixture.user();
		Category category = category(user);

		String longMemo = "a".repeat(301);

		//when & then
		Article article = Article.create("test", longMemo, user, category,
			LocalDateTime.of(2025, 7, 7, 12, 0, 0));
		assertThat(article.isRead()).isFalse();
	}
}
