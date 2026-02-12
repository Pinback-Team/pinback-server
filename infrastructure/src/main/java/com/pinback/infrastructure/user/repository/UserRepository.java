package com.pinback.infrastructure.user.repository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pinback.domain.user.entity.User;
import com.pinback.domain.user.enums.Job;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	@Modifying
	@Query("UPDATE User u SET u.remindDefault = :newRemindDefault WHERE u.id = :userId")
	void updateRemindDefault(@Param("userId") UUID userId, @Param("newRemindDefault") LocalTime newRemindDefault);

	@Modifying
	@Query("UPDATE User u SET u.profileImage = :imageProfile WHERE u.id = :userId")
	void updateProfileImage(@Param("userId") UUID userId, @Param("imageProfile") String imageProfile);

	@Modifying
	@Query("UPDATE User u SET u.job = :job WHERE u.id = :userId")
	void updateJob(@Param("userId") UUID userId, @Param("job") Job job);
}
