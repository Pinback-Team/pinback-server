package com.pinback.domain.article.service;

import static com.pinback.domain.fixture.TestFixture.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.ServiceTest;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.article.repository.ArticleRepository;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.repository.CategoryRepository;
import com.pinback.domain.user.entity.User;
import com.pinback.domain.user.repository.UserRepository;

@Import({ArticleDeleteService.class})
@Transactional
class ArticleDeleteServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleDeleteService articleDeleteService;

	@DisplayName("아티클 ID로 삭제하면 데이터베이스에서 제거된다.")
	@Test
	void deleteByIdTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = articleRepository.save(article(user, "test-url", category));
		Long articleId = article.getId();

		//when
		articleDeleteService.deleteById(articleId);

		//then
		boolean exists = articleRepository.existsById(articleId);
		Assertions.assertThat(exists).isFalse();
	}

	@DisplayName("존재하지 않는 아티클 ID로 삭제해도 예외가 발생하지 않는다.")
	@Test
	void deleteNonExistentArticleTest() {
		//given
		Long nonExistentId = 999L;

		//when & then
		Assertions.assertThatCode(() -> articleDeleteService.deleteById(nonExistentId))
			.doesNotThrowAnyException();
	}
}