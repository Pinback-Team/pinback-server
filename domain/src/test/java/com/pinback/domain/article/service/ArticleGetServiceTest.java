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
	void checkExistsByUserAndUrlTes() {
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

}
