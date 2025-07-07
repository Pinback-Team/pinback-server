package com.pinback.pinback_server.domain;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.pinback.pinback_server.domain.fixture.CustomRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {

	@Autowired
	CustomRepository customRepository;

	@AfterEach
	void tearDown() {
		customRepository.clearAndReset();
	}
}
