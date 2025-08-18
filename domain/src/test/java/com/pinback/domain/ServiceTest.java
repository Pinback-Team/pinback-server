package com.pinback.domain;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.pinback.domain.fixture.CustomRepository;
import com.pinback.global.QueryDslConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({CustomRepository.class, QueryDslConfig.class})
@ActiveProfiles("test")
public class ServiceTest {

	@Autowired
	CustomRepository customRepository;

	@AfterEach
	void tearDown() {
		customRepository.clearAndReset();
	}
}
