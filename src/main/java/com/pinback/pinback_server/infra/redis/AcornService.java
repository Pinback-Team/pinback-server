package com.pinback.pinback_server.infra.redis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.pinback.pinback_server.domain.user.domain.entity.User;
import com.pinback.pinback_server.infra.redis.dto.response.AcornCollectResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AcornService {

	private static final long MAX_ACORNS_PER_DAY = 7;
	private static final String REDIS_KEY_PREFIX = "daily_acorns:";
	private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");
	private final StringRedisTemplate redisTemplate;

	public int getCurrentAcorns(UUID userId) {
		String key = REDIS_KEY_PREFIX + userId.toString();
		String acornsStr = redisTemplate.opsForValue().get(key);

		return Optional.ofNullable(acornsStr)
			.map(Integer::parseInt) // String -> int 변환
			.orElse(0); // null -> 0 반환
	}

	public AcornCollectResponse tryCollectAcorns(User user) {
		String key = REDIS_KEY_PREFIX + user.getId();
		int currentAcorns = getCurrentAcorns(user.getId());
		int finalAcorns;
		boolean isCollected;

		if (currentAcorns < MAX_ACORNS_PER_DAY) {
			finalAcorns = currentAcorns + 1;
			long ttlSeconds = calculateTtlSeconds(user);
			redisTemplate.opsForValue().set(key, String.valueOf(finalAcorns), ttlSeconds, TimeUnit.SECONDS);
			isCollected = true;
			log.info("사용자 {}가 도토리 1개를 획득했습니다. 최종 도토리: {}, TTL: {} 초", user.getId(), finalAcorns, ttlSeconds);
		} else {
			finalAcorns = currentAcorns;
			isCollected = false;
			log.info("사용자 {}는 일일 도토리 한도({})에 도달하였습니다. 최종 도토리: {}", user.getId(), MAX_ACORNS_PER_DAY, finalAcorns);
		}
		return AcornCollectResponse.builder()
			.finalAcornCount(finalAcorns)
			.isCollected(isCollected)
			.build();
	}

	public void resetAcornsForTest(UUID userId) {
		String key = REDIS_KEY_PREFIX + userId.toString();
		Boolean deleted = redisTemplate.delete(key);

		if (deleted != null && deleted) {
			log.info("TEST_RESET: 사용자 {}의 도토리 개수를 0으로 초기화했습니다 (Redis 키 '{}' 삭제됨).", userId, key);
		} else {
			log.warn("TEST_RESET: 사용자 {}의 도토리 개수 초기화 시도했으나, Redis 키 '{}'를 찾을 수 없거나 삭제되지 않았습니다.", userId, key);
		}
	}

	private long calculateTtlSeconds(User user) {
		LocalTime reminderTime = user.getRemindDefault();

		LocalDateTime now = LocalDateTime.now(SEOUL_ZONE_ID);
		LocalDateTime nextReminderDateTime;

		if (now.toLocalTime().isBefore(reminderTime)) {
			nextReminderDateTime = now.with(reminderTime);
		} else {
			nextReminderDateTime = now.plusDays(1).with(reminderTime);
		}

		long ttl = Duration.between(now, nextReminderDateTime).getSeconds();
		log.info("ttl : {}", ttl);
		return Math.max(1, ttl);
	}
}
