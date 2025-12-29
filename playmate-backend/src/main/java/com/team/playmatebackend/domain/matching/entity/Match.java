package com.team.playmatebackend.domain.matching.entity;

import com.team.playmatebackend.domain.matching.entity.enums.EntryMethod;
import com.team.playmatebackend.domain.matching.entity.enums.SkillLevel;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 매칭방 엔티티
 * 방장의 조건 공개 의무와 참여 인원 조절 기능 포함
 *
 * @author 허준형
 * @DateOfCreated 2025-12-28
 * @DateOfEdit 2025-12-28
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방 제목
    @Column(nullable = false)
    private String title;

    // 작성자 (방장)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    // 원하는 컨텐츠
    @Enumerated(EnumType.STRING)
    private PreferCategory category;

    // 최대 인원 수
    private int maxParticipants;
    // 현재 인원 수
    private int currentParticipants;

    // 입장 방식 (즉시/승인)
    @Enumerated(EnumType.STRING)
    private EntryMethod entryMethod;

    // 방장이 설정한 제한 조건들
    // 성별
    @Enumerated(EnumType.STRING)
    private Gender genderRestriction;

    // 나이대
    @Enumerated(EnumType.STRING)
    private AgeGroup ageRestriction;

    // 실력
    @Enumerated(EnumType.STRING)
    private SkillLevel skillRestriction;

    @CreatedDate
    private LocalDateTime createdDate;

    // 매칭방 생성 시 디스코드 혹은 연락할 수 있는 곳을 적어두는 용도
    private String contactInfo;

    // 매칭방 태그
    private String hashTag;

    /**
     * 인원이 추가될 때 현재 인원수를 1 증가시키는 비즈니스 로직
     * FIXME: 한 번에 여러명이 동시에 신청을 하게되면 동시성 이슈가 발생할 수 있어서 해결 필요
     *
     * @author 허준형
     * @DateOfCreated 2025-12-28
     * @DateOfEdit 2025-12-28
     */
    public void addParticipant() {
        if (this.currentParticipants >= this.maxParticipants) {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }
        this.currentParticipants++;
    }

    /**
     * 참여자가 나갈 때 인원이 감소하는 메서드
     * FIXME: 인원 증가 메서드와 똑같이 동시성 이슈가 발생할 수 있음
     * @author 허준형
     * @DateOfCreated 2025-12-28
     * @DateOfEdit 2025-12-28
     */
    public void removeParticipant() {
        if (this.currentParticipants > 0) {
            this.currentParticipants--;
        }
    }
}