package com.pinback.pinback_server.domain.user.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pinback.pinback_server.domain.user.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsUserByEmail(String email);
}
