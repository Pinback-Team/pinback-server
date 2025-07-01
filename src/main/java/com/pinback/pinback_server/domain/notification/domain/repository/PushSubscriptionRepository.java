package com.pinback.pinback_server.domain.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.notification.domain.entity.PushSubscription;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {
}
