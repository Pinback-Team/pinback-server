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
import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.article.exception.ArticleAlreadyExistException;
import com.pinback.pinback_server.domain.article.presentation.dto.response.ArticleDetailResponse;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
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

	@DisplayName("사용자는 아티클을 생성할 수 있다.")
	@Test
	void articleSaveService() {
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		ArticleCreateCommand command = new ArticleCreateCommand("testUrl", category.getId()
			, "테스트메모",
			LocalDateTime.of(2025, 8, 6, 0, 0, 0));
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

}
