package com.team.playmatebackend.domain.user.dto;

import com.team.playmatebackend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 패스워드 재설정 링크에 들어갈 토큰
    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 패스워드 재설정 토큰 만료시간
    private LocalDateTime expiredAt;

    // 토큰의 재사용 방지용
    private boolean used;

    public void use() {
        this.used = true;
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
