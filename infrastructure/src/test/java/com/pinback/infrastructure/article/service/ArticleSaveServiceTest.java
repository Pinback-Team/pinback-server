package com.pinback.infrastructure.article.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.ServiceTest;
import com.pinback.infrastructure.article.repository.ArticleRepository;
import com.pinback.infrastructure.category.repository.CategoryRepository;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import(ArticleSaveService.class)
@Transactional
class ArticleSaveServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleSaveService articleSaveService;

	@DisplayName("아티클을 저장하면 ID가 생성되어 반환된다.")
	@Test
	void saveArticleTest() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = article(user, "test-url", category);

		//when
		Article savedArticle = articleSaveService.save(article);

		//then
		Assertions.assertThat(savedArticle.getId()).isNotNull();
		Assertions.assertThat(savedArticle.getUrl()).isEqualTo("test-url");
		Assertions.assertThat(savedArticle.getUser()).isEqualTo(user);
		Assertions.assertThat(savedArticle.getCategory()).isEqualTo(category);
	}

	@DisplayName("저장된 아티클은 데이터베이스에서 조회할 수 있다.")
	@Test
	void savedArticleCanBeRetrieved() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		Article article = article(user, "test-url-2", category);

		//when
		Article savedArticle = articleSaveService.save(article);

		//then
		Article foundArticle = articleRepository.findById(savedArticle.getId()).orElse(null);
		Assertions.assertThat(foundArticle).isNotNull();
		Assertions.assertThat(foundArticle.getUrl()).isEqualTo("test-url-2");
	}
}
