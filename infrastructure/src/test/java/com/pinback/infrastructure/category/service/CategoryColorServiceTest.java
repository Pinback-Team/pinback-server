package com.pinback.infrastructure.category.service;

import static com.pinback.infrastructure.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

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

@Import(CategoryColorService.class)
@Transactional
class CategoryColorServiceTest extends ServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryColorService categoryColorService;

	@DisplayName("사용자가 사용한 색상 목록을 조회할 수 있다")
	@Test
	void getUsedColorsByUser_Success() {
		// given
		User user = userRepository.save(user());
		categoryRepository.save(Category.createWithIsPublic("카테고리1", user, CategoryColor.COLOR1, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리2", user, CategoryColor.COLOR3, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리3", user, CategoryColor.COLOR5, true));

		// when
		Set<CategoryColor> usedColors = categoryColorService.getUsedColorsByUser(user);

		// then
		assertThat(usedColors).hasSize(3);
		assertThat(usedColors).contains(CategoryColor.COLOR1, CategoryColor.COLOR3, CategoryColor.COLOR5);
		assertThat(usedColors).doesNotContain(CategoryColor.COLOR2, CategoryColor.COLOR4);
	}

	@DisplayName("사용자가 카테고리를 하나도 생성하지 않으면 빈 Set을 반환한다")
	@Test
	void getUsedColorsByUser_NoCategories() {
		// given
		User user = userRepository.save(user());

		// when
		Set<CategoryColor> usedColors = categoryColorService.getUsedColorsByUser(user);

		// then
		assertThat(usedColors).isEmpty();
	}

	@DisplayName("한 사용자는 각 색상을 한 번만 사용할 수 있다")
	@Test
	void getUsedColorsByUser_UniqueColorsPerUser() {
		// given
		User user = userRepository.save(user());
		categoryRepository.save(Category.createWithIsPublic("카테고리1", user, CategoryColor.COLOR1, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리2", user, CategoryColor.COLOR2, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리3", user, CategoryColor.COLOR3, true));

		// when
		Set<CategoryColor> usedColors = categoryColorService.getUsedColorsByUser(user);

		// then
		assertThat(usedColors).hasSize(3);
		assertThat(usedColors).contains(CategoryColor.COLOR1, CategoryColor.COLOR2, CategoryColor.COLOR3);
	}

	@DisplayName("다른 사용자의 카테고리 색상은 조회되지 않는다")
	@Test
	void getUsedColorsByUser_DifferentUsers() {
		// given
		User user1 = userRepository.save(user());
		User user2 = userRepository.save(userWithEmail("user2@test.com"));

		categoryRepository.save(Category.createWithIsPublic("카테고리1", user1, CategoryColor.COLOR1, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리2", user1, CategoryColor.COLOR2, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리3", user2, CategoryColor.COLOR3, true));
		categoryRepository.save(Category.createWithIsPublic("카테고리4", user2, CategoryColor.COLOR4, true));

		// when
		Set<CategoryColor> user1Colors = categoryColorService.getUsedColorsByUser(user1);
		Set<CategoryColor> user2Colors = categoryColorService.getUsedColorsByUser(user2);

		// then
		assertThat(user1Colors).hasSize(2);
		assertThat(user1Colors).contains(CategoryColor.COLOR1, CategoryColor.COLOR2);
		assertThat(user1Colors).doesNotContain(CategoryColor.COLOR3, CategoryColor.COLOR4);

		assertThat(user2Colors).hasSize(2);
		assertThat(user2Colors).contains(CategoryColor.COLOR3, CategoryColor.COLOR4);
		assertThat(user2Colors).doesNotContain(CategoryColor.COLOR1, CategoryColor.COLOR2);
	}

	@DisplayName("모든 색상을 사용한 경우 모든 색상이 반환된다")
	@Test
	void getUsedColorsByUser_AllColors() {
		// given
		User user = userRepository.save(user());
		CategoryColor[] allColors = CategoryColor.values();

		for (int i = 0; i < allColors.length; i++) {
			categoryRepository.save(Category.createWithIsPublic("카테고리" + (i + 1), user, allColors[i], true));
		}

		// when
		Set<CategoryColor> usedColors = categoryColorService.getUsedColorsByUser(user);

		// then
		assertThat(usedColors).hasSize(allColors.length);
		assertThat(usedColors).contains(allColors);
	}

	@DisplayName("사용자별로 독립적인 색상 목록을 관리한다")
	@Test
	void getUsedColorsByUser_IndependentColorManagement() {
		// given
		User user1 = userRepository.save(user());
		User user2 = userRepository.save(userWithEmail("user2@test.com"));

		// user1은 COLOR1~COLOR5 사용
		for (int i = 1; i <= 5; i++) {
			CategoryColor color = CategoryColor.valueOf("COLOR" + i);
			categoryRepository.save(Category.createWithIsPublic("usr1카테고리" + i, user1, color, true));
		}

		// user2는 COLOR6~COLOR10 사용
		for (int i = 6; i <= 10; i++) {
			CategoryColor color = CategoryColor.valueOf("COLOR" + i);
			categoryRepository.save(Category.createWithIsPublic("usr2카테고리" + i, user2, color, true));
		}

		// when
		Set<CategoryColor> user1Colors = categoryColorService.getUsedColorsByUser(user1);
		Set<CategoryColor> user2Colors = categoryColorService.getUsedColorsByUser(user2);

		// then
		assertThat(user1Colors).hasSize(5);
		assertThat(user1Colors).contains(
			CategoryColor.COLOR1, CategoryColor.COLOR2, CategoryColor.COLOR3,
			CategoryColor.COLOR4, CategoryColor.COLOR5
		);

		assertThat(user2Colors).hasSize(5);
		assertThat(user2Colors).contains(
			CategoryColor.COLOR6, CategoryColor.COLOR7, CategoryColor.COLOR8,
			CategoryColor.COLOR9, CategoryColor.COLOR10
		);

		assertThat(user1Colors).doesNotContain(
			CategoryColor.COLOR6, CategoryColor.COLOR7, CategoryColor.COLOR8,
			CategoryColor.COLOR9, CategoryColor.COLOR10
		);

		assertThat(user2Colors).doesNotContain(
			CategoryColor.COLOR1, CategoryColor.COLOR2, CategoryColor.COLOR3,
			CategoryColor.COLOR4, CategoryColor.COLOR5
		);
	}
}
