package com.pinback.domain.article.service;

import static com.pinback.domain.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

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

		//when
		articleDeleteService.delete(article);

		//then
		boolean exists = articleRepository.existsById(article.getId());
		assertThat(exists).isFalse();
	}

	@DisplayName("유저ID와 카테고리ID로 해당 카테고리의 모든 아티클을 삭제할 수 있다.")
	@Test
	void deleteAllByCategory() {
		//given
		User user = userRepository.save(user());
		Category category = categoryRepository.save(category(user));
		articleRepository.save(article(user, "test-url", category));
		articleRepository.save(article(user, "test-url2", category));
		articleRepository.save(article(user, "test-url3", category));

		//when
		articleDeleteService.deleteAllByCategory(user.getId(), category.getId());

		//then
		assertThat(articleRepository.findAll().size()).isEqualTo(0);
	}

}