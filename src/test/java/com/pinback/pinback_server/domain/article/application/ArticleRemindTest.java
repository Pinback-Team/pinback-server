package com.pinback.pinback_server.domain.article.application;

import static com.pinback.pinback_server.domain.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.ApplicationTest;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.article.presentation.dto.response.RemindArticleResponse;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ArticleRemindTest extends ApplicationTest {
	@Autowired
	private ArticleManagementUsecase articleManagementUsecase;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ArticleRepository articleRepository;

	@DisplayName("오늘 기준을 24시간 이내의 리마인드 아티클 들을 조회한다.")
	@Test
	void getRemindArticleInRange() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));

		//리마인드 시간이 7월 7일 9시 1분, 7월 8일 9시 이라고 가정
		// 오늘이 7월 8일 9시라고 가정
		articleRepository.save(
			articleWithDate(user, "article" + "1", category, LocalDateTime.of(2025, 7, 7, 9, 1, 0)));

		articleRepository.save(
			articleWithDate(user, "article" + "2", category, LocalDateTime.of(2025, 7, 8, 9, 0, 0)));

		//when
		RemindArticleResponse responses = articleManagementUsecase.getRemindArticles(user,
			LocalDateTime.of(2025, 7, 8, 9, 0, 0), 0, 5);

		//then
		assertThat(responses.totalArticle())
			.isEqualTo(2);

		assertThat(responses.nextRemind())
			.isEqualTo("2025년 07월 09일 오후 12시 00분");

		assertThat(responses.articles().get(1).remindAt()).isEqualTo(LocalDateTime.of(2025, 7, 7, 9, 1, 0));

	}

	@DisplayName("오늘 기준을 24시간 범위 외의 아티클들을 조회할 수 없다.")
	@Test
	void getRemindArticleOutRange() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));

		//리마인드 시간이 7월 7일 9시 0분, 7월 8일 9시 이라고 가정
		// 오늘이 7월 8일 9시라고 가정
		articleRepository.save(
			articleWithDate(user, "article" + "1", category, LocalDateTime.of(2025, 7, 7, 9, 0, 0)));

		articleRepository.save(
			articleWithDate(user, "article" + "2", category, LocalDateTime.of(2025, 7, 8, 9, 1, 0)));

		//when
		RemindArticleResponse responses = articleManagementUsecase.getRemindArticles(user,
			LocalDateTime.of(2025, 7, 8, 9, 0, 0), 0, 5);

		//then
		assertThat(responses.totalArticle())
			.isEqualTo(0);

	}

	@DisplayName("사용자는 다음날이 다음 달로 넘어가는 경우 다음 리마인드 날짜를 조회할 수 있다.")
	@Test
	void getRemindWhenMonthOver() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));

		//when
		RemindArticleResponse responses = articleManagementUsecase.getRemindArticles(user,
			LocalDateTime.of(2025, 7, 31, 9, 0, 0), 0, 5);

		//then
		assertThat(responses.nextRemind())
			.isEqualTo("2025년 08월 01일 오후 12시 00분");
		//2025년 8월 1일 오후 12시 00분

	}
}
