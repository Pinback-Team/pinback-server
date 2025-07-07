package com.pinback.pinback_server.domain.article.application;

import static com.pinback.pinback_server.domain.fixture.TestFixture.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.pinback_server.domain.article.application.command.ArticleCreateCommand;
import com.pinback.pinback_server.domain.article.domain.entity.Article;
import com.pinback.pinback_server.domain.article.domain.repository.ArticleRepository;
import com.pinback.pinback_server.domain.category.domain.entity.Category;
import com.pinback.pinback_server.domain.category.domain.repository.CategoryRepository;
import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.domain.user.domain.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ArticleManagementUsecaseTest {

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
		Assertions.assertThat(article.getUrl()).isEqualTo(command.url());
		Assertions.assertThat(article.getMemo()).isEqualTo(command.memo());
		Assertions.assertThat(article.getCategory()).isEqualTo(category);
		Assertions.assertThat(article.getIsRead()).isFalse();
	}
}
