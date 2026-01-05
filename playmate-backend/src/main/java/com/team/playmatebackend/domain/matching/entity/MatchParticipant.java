package com.team.playmatebackend.domain.matching.entity;

import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.matching.entity.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 참가자가 방장의 승인을 대기 중인지, 승인 되었는지 관리하기 위한 참가자 상태 엔티티
 *
 * @author 허준형
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    public void approve() {
        this.status = ParticipantStatus.ACCEPTED;
    }

    public void reject() {
        this.status = ParticipantStatus.REJECTED;
    }
}