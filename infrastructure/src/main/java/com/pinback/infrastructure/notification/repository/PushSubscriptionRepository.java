package com.pinback.infrastructure.notification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pinback.domain.notification.entity.PushSubscription;
import com.pinback.domain.user.entity.User;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

	List<PushSubscription> findByUserId(UUID userId);

	Optional<PushSubscription> findByUser(User user);

	Optional<PushSubscription> findByToken(String token);

	boolean existsByToken(String token);

	void deleteByUserId(UUID userId);

	void deleteByUser(User user);
}
