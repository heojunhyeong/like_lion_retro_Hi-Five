/**
 *
 * 로그아웃 처리를 위한 User 조회 메서드 추가
 * SecurityContext 기준 사용자 조회를 위해 findByUserId 추가
 *
 * @author 김지번
 * @DateOfCreated 2025-12-23
 * @DateOfEdit 2025-12-23
 */

package com.team.playmatebackend.domain.user.repository;

import com.team.playmatebackend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findById(String username);
    Boolean existsByUserId(String userid);
    Boolean existsByNickName(String nickName);
    Boolean existsByUserEmail(String userEmail);


    Optional<User> findByUserId(String userId);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    Optional<User> findByEmail(String email);
}