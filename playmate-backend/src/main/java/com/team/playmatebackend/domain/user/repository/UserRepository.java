package com.team.playmatebackend.domain.user.repository;

import com.team.playmatebackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserId(String userid);
    Boolean existByNickname(String nickname);
    Boolean existByEmail(String email);
}