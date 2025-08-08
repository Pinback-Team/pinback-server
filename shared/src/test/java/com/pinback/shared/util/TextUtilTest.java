package com.pinback.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextUtilTest {

	@DisplayName("null 텍스트는 0을 반환한다")
	@Test
	void countGraphemeClusters_nullText_returnsZero() {
		int result = TextUtil.countGraphemeClusters(null);
		assertThat(result).isEqualTo(0);
	}

	@DisplayName("빈 텍스트는 0을 반환한다")
	@Test
	void countGraphemeClusters_emptyText_returnsZero() {
		int result = TextUtil.countGraphemeClusters("");
		assertThat(result).isEqualTo(0);
	}

	@DisplayName("단일 문자는 1을 반환한다")
	@Test
	void countGraphemeClusters_singleCharacter_returnsOne() {
		int result = TextUtil.countGraphemeClusters("a");
		assertThat(result).isEqualTo(1);
	}

	@DisplayName("영어 문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_multipleAsciiCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("hello");
		assertThat(result).isEqualTo(5);
	}

	@DisplayName("한국어 문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_koreanCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("안녕하세요");
		assertThat(result).isEqualTo(5);
	}

	@DisplayName("혼합된 문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_mixedCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("hello 안녕");
		assertThat(result).isEqualTo(8);
	}

	@DisplayName("이모지 문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_emojiCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("😀😃😄");
		assertThat(result).isEqualTo(3);
	}

	@DisplayName("복합 이모지를 하나의 문자로 계산한다")
	@Test
	void countGraphemeClusters_complexEmoji_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("👨‍👩‍👧‍👦");
		assertThat(result).isEqualTo(1);
	}

	@DisplayName("공백 문자를 포함한 문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_whitespaceCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("a b c");
		assertThat(result).isEqualTo(5);
	}

	@DisplayName("특수문자들의 개수를 정확히 계산한다")
	@Test
	void countGraphemeClusters_specialCharacters_returnsCorrectCount() {
		int result = TextUtil.countGraphemeClusters("!@#$%");
		assertThat(result).isEqualTo(5);
	}
}