package com.team.playmatebackend.domain.user.repository;

import com.team.playmatebackend.domain.user.dto.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 패스워드 찾기 메서드에 사용될 쿼리
 * 토큰 문자열로 조회
 * @author 허준형
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 */

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
