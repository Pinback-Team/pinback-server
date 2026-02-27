package com.pinback.infrastructure.category.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pinback.domain.category.entity.Category;
import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;
import com.pinback.infrastructure.ServiceTest;
import com.pinback.infrastructure.category.repository.CategoryRepository;
import com.pinback.infrastructure.user.repository.UserRepository;

@Import(CategorySaveService.class)
@Transactional
class CategorySaveServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategorySaveService categorySaveService;

	@DisplayName("카테고리를 데이터베이스에 저장할 수 있다")
	@Test
	void saveSuccess() {
		// given
		User user = userRepository.save(user());
		Category category = Category.createWithIsPublic("테스트카테고리", user, CategoryColor.COLOR1, true);

		// when
		Category savedCategory = categorySaveService.save(category);

		// then
		assertThat(savedCategory.getId()).isNotNull();
		assertThat(savedCategory.getName()).isEqualTo("테스트카테고리");
		assertThat(savedCategory.getUser()).isEqualTo(user);
		assertThat(savedCategory.getColor()).isEqualTo(CategoryColor.COLOR1);

		Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
		assertThat(foundCategory).isNotNull();
		assertThat(foundCategory.getName()).isEqualTo("테스트카테고리");
		assertThat(foundCategory.getColor()).isEqualTo(CategoryColor.COLOR1);
	}

	@DisplayName("여러 카테고리를 서로 다른 색상으로 저장할 수 있다")
	@Test
	void saveMultipleCategories_DifferentColors() {
		// given
		User user = userRepository.save(user());
		Category category1 = Category.createWithIsPublic("카테고리1", user, CategoryColor.COLOR1, true);
		Category category2 = Category.createWithIsPublic("카테고리2", user, CategoryColor.COLOR2, true);
		Category category3 = Category.createWithIsPublic("카테고리3", user, CategoryColor.COLOR3, true);

		// when
		Category savedCategory1 = categorySaveService.save(category1);
		Category savedCategory2 = categorySaveService.save(category2);
		Category savedCategory3 = categorySaveService.save(category3);

		// then
		assertThat(savedCategory1.getColor()).isEqualTo(CategoryColor.COLOR1);
		assertThat(savedCategory2.getColor()).isEqualTo(CategoryColor.COLOR2);
		assertThat(savedCategory3.getColor()).isEqualTo(CategoryColor.COLOR3);

		Category found1 = categoryRepository.findById(savedCategory1.getId()).orElse(null);
		Category found2 = categoryRepository.findById(savedCategory2.getId()).orElse(null);
		Category found3 = categoryRepository.findById(savedCategory3.getId()).orElse(null);

		assertThat(found1.getColor()).isEqualTo(CategoryColor.COLOR1);
		assertThat(found2.getColor()).isEqualTo(CategoryColor.COLOR2);
		assertThat(found3.getColor()).isEqualTo(CategoryColor.COLOR3);
	}

	@DisplayName("다른 사용자는 같은 색상의 카테고리를 저장할 수 있다")
	@Test
	void saveSameColorCategories_DifferentUsers() {
		// given
		User user1 = userRepository.save(user());
		User user2 = userRepository.save(userWithEmail("user2@test.com"));
		Category category1 = Category.createWithIsPublic("카테고리1", user1, CategoryColor.COLOR1, true);
		Category category2 = Category.createWithIsPublic("카테고리2", user2, CategoryColor.COLOR1, true);

		// when
		Category savedCategory1 = categorySaveService.save(category1);
		Category savedCategory2 = categorySaveService.save(category2);

		// then
		assertThat(savedCategory1.getColor()).isEqualTo(CategoryColor.COLOR1);
		assertThat(savedCategory2.getColor()).isEqualTo(CategoryColor.COLOR1);
		assertThat(savedCategory1.getUser()).isEqualTo(user1);
		assertThat(savedCategory2.getUser()).isEqualTo(user2);
	}

	@DisplayName("같은 사용자가 같은 색상의 카테고리를 저장하려고 하면 제약조건 위반으로 예외가 발생한다")
	@Test
	void saveSameColorCategories_SameUser_ThrowsException() {
		// given
		User user = userRepository.save(user());
		Category category1 = Category.createWithIsPublic("카테고리1", user, CategoryColor.COLOR1, true);
		categorySaveService.save(category1);

		Category category2 = Category.createWithIsPublic("카테고리2", user, CategoryColor.COLOR1, true);

		// when & then
		assertThatThrownBy(() -> categorySaveService.save(category2))
			.isInstanceOf(Exception.class);
	}

	@DisplayName("색상이 null이면 저장할 수 없다")
	@Test
	void save_NullColor_ThrowsException() {
		// given
		User user = userRepository.save(user());
		Category category = Category.createWithIsPublic("테스트카테고리", user, null, true);

		// when & then
		assertThatThrownBy(() -> categorySaveService.save(category))
			.isInstanceOf(Exception.class);
	}
}
