package com.pinback.domain.article.service;

import static com.pinback.domain.fixture.TestFixture.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.ServiceTest;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.article.exception.ArticleNotFoundException;
import com.pinback.domain.article.repository.ArticleRepository;
import com.pinback.domain.article.repository.dto.ArticlesWithUnreadCount;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.repository.CategoryRepository;
import com.pinback.domain.user.entity.User;
import com.pinback.domain.user.repository.UserRepository;

@Import({ArticleGetService.class})
@Transactional
class ArticleGetServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ArticleGetService articleGetService;

	@DisplayName("유저가 같은 URL을 가진 아티클이 존재하는 경우 참을 반환한다.")
	@Test
	void checkExistsByUserAndUrlTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		String url = "test-url";
		Article article = article(user, url, category);
		articleRepository.save(article);

		//when
		boolean isExist = articleGetService.checkExistsByUserAndUrl(user, url);

		//then
		Assertions.assertThat(isExist).isTrue();
	}

	@DisplayName("유저가 다른 URL을 가진 아티클이 존재하지 않는 경우 거짓을 반환한다.")
	@Test
	void checkExistsByUserAndUrlNotExistTest() {
		//given
		User user = userRepository.save(user());
		String url = "non-existent-url";

		//when
		boolean isExist = articleGetService.checkExistsByUserAndUrl(user, url);

		//then
		Assertions.assertThat(isExist).isFalse();
	}

	@DisplayName("아티클 ID로 조회하면 아티클을 반환한다.")
	@Test
	void findByIdTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(article(user, "test-url", category));

		//when
		Article foundArticle = articleGetService.findById(article.getId());

		//then
		Assertions.assertThat(foundArticle.getId()).isEqualTo(article.getId());
		Assertions.assertThat(foundArticle.getUrl()).isEqualTo("test-url");
	}

	@DisplayName("존재하지 않는 아티클 ID로 조회하면 예외가 발생한다.")
	@Test
	void findByIdNotFoundTest() {
		//given
		Long nonExistentId = 999L;

		//when & then
		Assertions.assertThatThrownBy(() -> articleGetService.findById(nonExistentId))
			.isInstanceOf(ArticleNotFoundException.class);
	}

	@DisplayName("유저 ID로 모든 아티클을 조회하면 아티클과 읽지 않은 개수를 반환한다.")
	@Test
	void findAllTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		articleRepository.save(article(user, "url1", category));
		articleRepository.save(readArticle(user, "url2", category));
		PageRequest pageRequest = PageRequest.of(0, 10);

		//when
		ArticlesWithUnreadCount result = articleGetService.findAll(user.getId(), pageRequest);

		//then
		Assertions.assertThat(result.getUnReadCount()).isEqualTo(1L);
		Assertions.assertThat(result.getArticle().getContent()).hasSize(2);
	}

	@DisplayName("카테고리별로 아티클을 조회하면 해당 카테고리의 아티클만 반환한다.")
	@Test
	void findAllByCategoryTest() {
		//given
		User user = userRepository.save(user());
		Category category1 = categoryRepository.save(category(user));
		Category category2 = categoryRepository.save(Category.create("다른카테고리", user));
		
		articleRepository.save(article(user, "url1", category1));
		articleRepository.save(article(user, "url2", category2));
		PageRequest pageRequest = PageRequest.of(0, 10);

		//when
		ArticlesWithUnreadCount result = articleGetService.findAllByCategory(user.getId(), category1, pageRequest);

		//then
		Assertions.assertThat(result.getArticle().getContent()).hasSize(1);
		Assertions.assertThat(result.getArticle().getContent().get(0).getCategory().getId()).isEqualTo(category1.getId());
	}

	@DisplayName("오늘 리마인드할 아티클을 조회하면 해당 시간대의 아티클만 반환한다.")
	@Test
	void findTodayRemindTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		LocalDateTime today = LocalDateTime.of(2025, 8, 18, 12, 0, 0);
		LocalDateTime yesterday = today.minusDays(1);
		
		articleRepository.save(articleWithDate(user, "url1", category, today));
		articleRepository.save(articleWithDate(user, "url2", category, yesterday));
		PageRequest pageRequest = PageRequest.of(0, 10);

		//when
		Page<Article> result = articleGetService.findTodayRemind(user.getId(), today, pageRequest);

		//then
		Assertions.assertThat(result.getContent()).hasSize(1);
		Assertions.assertThat(result.getContent().get(0).getRemindAt()).isEqualTo(today);
	}

	@DisplayName("유저의 가장 최근 아티클을 조회한다.")
	@Test
	void findRecentByUserTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		articleRepository.save(article(user, "url1", category));

		//when
		Optional<Article> result = articleGetService.findRecentByUser(user);

		//then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getUrl()).isEqualTo("url1");
	}

	@DisplayName("URL과 유저로 아티클을 조회한다.")
	@Test
	void findByUrlAndUserTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		String url = "specific-url";
		articleRepository.save(article(user, url, category));

		//when
		Optional<Article> result = articleGetService.findByUrlAndUser(user, url);

		//then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getUrl()).isEqualTo(url);
	}

	@DisplayName("유저와 아티클 ID로 조회하면 아티클을 반환한다.")
	@Test
	void findByUserAndIdTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(article(user, "test-url", category));

		//when
		Article foundArticle = articleGetService.findByUserAndId(user, article.getId());

		//then
		Assertions.assertThat(foundArticle.getId()).isEqualTo(article.getId());
		Assertions.assertThat(foundArticle.getUser().getId()).isEqualTo(user.getId());
	}

	@DisplayName("다른 유저의 아티클을 조회하면 예외가 발생한다.")
	@Test
	void findByUserAndIdNotOwnedTest() {
		//given
		User user1 = userRepository.save(user());
		User user2 = userRepository.save(userWithEmail("other@gmail.com"));
		Category category = categoryRepository.save(category(user1));
		Article article = articleRepository.save(article(user1, "test-url", category));

		//when & then
		Assertions.assertThatThrownBy(() -> articleGetService.findByUserAndId(user2, article.getId()))
			.isInstanceOf(ArticleNotFoundException.class);
	}

	@DisplayName("읽지 않은 아티클만 조회하면 읽지 않은 아티클만 반환한다.")
	@Test
	void findAllByIsReadTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		articleRepository.save(article(user, "unread-url", category)); // 읽지 않음
		articleRepository.save(readArticle(user, "read-url", category)); // 읽음
		PageRequest pageRequest = PageRequest.of(0, 10);

		//when
		ArticlesWithUnreadCount result = articleGetService.findAllByIsRead(user.getId(), pageRequest);

		//then
		Assertions.assertThat(result.getUnReadCount()).isEqualTo(1L);
		Assertions.assertThat(result.getArticle().getContent()).hasSize(1);
		Assertions.assertThat(result.getArticle().getContent().get(0).isRead()).isFalse();
	}

}
