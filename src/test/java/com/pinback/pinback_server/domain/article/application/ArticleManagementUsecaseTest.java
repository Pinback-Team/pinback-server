package com.pinback.pinback_server.domain.article.application;

import static com.pinback.pinback_server.domain.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.ApplicationTest;
import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.article.exception.ArticleAlreadyExistException;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleAllResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticlesResponse;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.notification.domain.repository.PushSubscriptionRepository;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;
import com.pinback.pinback_server.infra.redis.service.RedisNotificationService;

@SpringBootTest
@Transactional
class ArticleManagementUsecaseTest extends ApplicationTest {

	@Autowired
	private ArticleManagementUsecase articleManagementUsecase;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private PushSubscriptionRepository pushSubscriptionRepository;
	@MockitoBean
	private RedisNotificationService redisNotificationService;

	@DisplayName("사용자는 아티클을 생성할 수 있다.")
	@Test
	void articleSaveService() {
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		pushSubscriptionRepository.save(pushSubscription(user));
		ArticleCreateCommand command = new ArticleCreateCommand("testUrl", category.getId()
			, "테스트메모",
			LocalDateTime.of(2025, 8, 6, 0, 0, 0));
		doNothing().when(redisNotificationService)
			.scheduleArticleReminder(any(), any(), any());
		//when
		articleManagementUsecase.createArticle(user, command);

		//then
		Article article = articleRepository.findById(1L).get();
		assertThat(article.getUrl()).isEqualTo(command.url());
		assertThat(article.getMemo()).isEqualTo(command.memo());
		assertThat(article.getCategory()).isEqualTo(category);
		assertThat(article.getRemindAt()).isEqualTo(command.remindTime());
		assertThat(article.getIsRead()).isFalse();
	}

	@DisplayName("사용자는 중복된 url을 저장할 수 없다.")
	@Test
	void articleDuplicate() {
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(articleWithCategory(user, category));
		ArticleCreateCommand command = new ArticleCreateCommand(article.getUrl(), article.getCategory().getId()
			, article.getMemo(),
			LocalDateTime.of(2025, 8, 6, 0, 0, 0));
		//when & then
		assertThatThrownBy(() -> articleManagementUsecase.createArticle(user, command))
			.isInstanceOf(ArticleAlreadyExistException.class);

	}

	@DisplayName("사용자는 아티클의 상세정보를 조회할 수 있다.")
	@Test
	void getArticleDetail() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(articleWithCategory(user, category));
		//when

		ArticleDetailResponse response = articleManagementUsecase.getArticleDetail(article.getId());

		//then
		assertThat(response.url()).isEqualTo(article.getUrl());
		assertThat(response.memo()).isEqualTo(article.getMemo());
		assertThat(response.category().categoryId()).isEqualTo(article.getCategory().getId());
		assertThat(response.category().categoryName()).isEqualTo(article.getCategory().getName());
		assertThat(response.remindAt()).isEqualTo(article.getRemindAt());
	}

	@DisplayName("사용자가 원하는 만큼 게시글 만큼 조회할 수 있다.")
	@Test
	void getAllArticle() {
		//given
		User user = userRepository.save(user());
		User user2 = userRepository.save(userWithEmail("test2"));
		Category category = categoryRepository.save(category(user));
		Category category2 = categoryRepository.save(category(user2));

		for (int i = 0; i < 12; i++) {
			articleRepository.save(article(user, Integer.toString(i), category));
		}

		for (int i = 0; i < 12; i++) {
			articleRepository.save(article(user2, Integer.toString(i), category2));
		}

		//when
		ArticleAllResponse responses = articleManagementUsecase.getAllArticles(user, 0, 5);

		//then
		assertThat(responses.articles()).hasSize(5);
		assertThat(responses.articles().get(0).createdAt()).isNotNull();
		assertThat(responses.totalArticle()).isEqualTo(12);

	}

	@DisplayName("게시글이 최신순으로 조회된다.")
	@Test
	void getAllArticleOrderByCreatedAtDesc() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));

		for (int i = 0; i < 10; i++) {
			articleRepository.save(article(user, "article" + i, category));
		}

		//when
		ArticleAllResponse responses = articleManagementUsecase.getAllArticles(user, 0, 5);

		//then
		assertThat(responses.articles())
			.hasSize(5)
			.extracting(ArticlesResponse::articleId)
			.isSortedAccordingTo(Comparator.reverseOrder());
	}

	@DisplayName("읽지 않은 게시글 수도 알려주어야 한다.")
	@Test
	void getAllWithUnReadArticles() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));

		for (int i = 0; i < 5; i++) {
			articleRepository.save(article(user, "article" + i, category));
		}

		for (int i = 0; i < 3; i++) {
			articleRepository.save(readArticle(user, "article2" + i, category));
		}

		//when
		ArticleAllResponse responses = articleManagementUsecase.getAllArticles(user, 0, 8);

		//then
		assertThat(responses.articles())
			.hasSize(8)
			.extracting(ArticlesResponse::articleId)
			.isSortedAccordingTo(Comparator.reverseOrder());

		assertThat(responses.totalUnreadArticle())
			.isEqualTo(5);
	}

	@DisplayName("카테고리 별로 게시글을 조회할 수 있다.")
	@Test
	void getByCategory() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Category category2 = categoryRepository.save(category(user));

		for (int i = 0; i < 5; i++) {
			articleRepository.save(article(user, "article" + i, category));
		}

		for (int i = 0; i < 5; i++) {
			articleRepository.save(article(user, "article2" + i, category2));
		}

		//when

		ArticleAllResponse responses = articleManagementUsecase.getAllArticlesByCategory(user, category.getId(), 0, 5);

		//then

		assertThat(responses.articles())
			.hasSize(5)
			.extracting(ArticlesResponse::articleId)
			.isSortedAccordingTo(Comparator.reverseOrder());

		assertThat(responses.totalArticle())
			.isEqualTo(5);

	}

	@DisplayName("카테고리 별로 게시글을 조회할 수 있다.")
	@Test
	void getByCategoryWithUnreadCount() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Category category2 = categoryRepository.save(category(user));

		for (int i = 0; i < 3; i++) {
			articleRepository.save(article(user, "article" + i, category));
		}

		for (int i = 0; i < 5; i++) {
			articleRepository.save(article(user, "article2" + i, category2));
		}

		for (int i = 0; i < 5; i++) {
			articleRepository.save(readArticle(user, "article3" + i, category));
		}

		//when

		ArticleAllResponse responses = articleManagementUsecase.getAllArticlesByCategory(user, category.getId(), 0, 5);

		//then

		assertThat(responses.totalUnreadArticle()).isEqualTo(3);
		assertThat(responses.totalArticle())
			.isEqualTo(8);
		assertThat(responses.articles().get(0).createdAt()).isNotNull();

	}

	@DisplayName("사용자는 url로 아티클의 존재여부를 확인할 수 있다.")
	@Test
	void checkArticleExists() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(articleWithCategory(user, category));
		//when

		ArticleDetailResponse response = articleManagementUsecase.checkArticleExists(user, article.getUrl());

		//then

		assertThat(response.url())
			.isEqualTo(article.getUrl());

	}

	@DisplayName("url에 해당하는 아티클이 존재하지 않는 경우 null을 반환한다.")
	@Test
	void checkArticleNotExistsReturnNull() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		articleRepository.save(articleWithCategory(user, category));
		//when

		ArticleDetailResponse response = articleManagementUsecase.checkArticleExists(user, "존재하지 않는 url");

		//then
		assertThat(response).isNull();
	}

}
