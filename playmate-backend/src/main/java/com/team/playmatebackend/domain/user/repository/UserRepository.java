package com.team.playmatebackend.domain.user.repository;

import com.team.playmatebackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUserId(String userid);
    Boolean existByNickname(String nickname);
    Boolean existByEmail(String email);
}