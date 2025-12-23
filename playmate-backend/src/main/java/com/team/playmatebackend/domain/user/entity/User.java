package com.team.playmatebackend.domain.user.entity;

import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 20)
    private String userPassword;

    @Column(nullable = false, unique = true, length = 30)
    private String userEmail;

    @Column(nullable = false, unique = true, length = 20)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private PreferCategory preferCategory;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup age;

    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @Column(length = 100)
    private String introduction;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}