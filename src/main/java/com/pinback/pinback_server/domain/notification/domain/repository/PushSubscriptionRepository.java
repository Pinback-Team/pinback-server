package com.pinback.pinback_server.domain.notification.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;
import com.pinback.pinback_server.domain.user.domain.entity.User;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

	Optional<PushSubscription> findPushSubscriptionByUser(User user);
}
