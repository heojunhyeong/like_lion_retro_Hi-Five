/**
 *
 * JWT 로그아웃을 위한 refreshToken 필드 및 무효화 로직 추가
 * User 엔티티에 refreshToken 컬럼 추가
 * logout() 메서드로 refreshToken 제거하여 로그인 상태 해제
 *
 * @author 김지번
 * @DateOfCreated 2025-12-23
 * @DateOfEdit 2025-12-23
 */

package com.team.playmatebackend.domain.user.entity;

import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 250)
    private String userPassword;

    @Column(unique = true)
    private String passwordResetToken;

    private LocalDateTime passwordResetExpiredAt;

    @Column(nullable = false, unique = true, length = 30)
    private String userEmail;

    @Column(nullable = false, unique = true, length = 20)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PreferCategory preferCategory;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup age;

    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @Column(length = 500)
    private String refreshToken;

    @Column(length = 100)
    private String introduction;

    public void logout() {
        this.refreshToken = null;
    }

    // 패스워드 찾기시 재설정 링크에 필요한 토큰
    public void issuePasswordResetToken(String token, LocalDateTime expiredAt) {
        this.passwordResetToken = token;
        this.passwordResetExpiredAt = expiredAt;
    }
    // 패스워드 변경 성공시 토큰 삭제(재사용 방지)
    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetExpiredAt = null;
    }

    public void changePassword(String encodedPassword) {
        this.userPassword = encodedPassword;
    }

    public void updateProfile(String nickName, PreferCategory preferCategory, String introduction) {
        this.nickName = nickName;
        this.preferCategory = preferCategory;
        this.introduction = introduction;
    }

}
