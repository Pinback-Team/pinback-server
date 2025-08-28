package com.pinback.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApplicationTestBase {
	// Application 계층 테스트는 Port만 Mock으로 사용
	// Infrastructure 의존성 없음
}
