package com.team.playmatebackend.domain.user.service;

import com.team.playmatebackend.domain.user.dto.UserProfileResponseDto;
import com.team.playmatebackend.domain.user.dto.UserProfileUpdateRequestDto;
import com.team.playmatebackend.domain.user.dto.UserProfileUpdateRequestDto;
import com.team.playmatebackend.domain.user.entity.User;
import com.team.playmatebackend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 사용자 프로필 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 로그인한 사용자의 프로필 조회 및 수정과 같은
 * 프로필 도메인 전용 로직을 담당한다.
 * 인증 정보는 컨트롤러로부터 전달받아 처리한다.
 *
 * @author 전진
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-29
 */
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    /**
     * 로그인한 사용자의 프로필 정보를 조회한다.
     *
     * JWT 인증을 통해 전달받은 userId(String)를 기준으로
     * 사용자 엔티티를 조회하고, 프로필 응답 DTO로 변환한다.
     *
     * @param userId 로그인한 사용자의 아이디
     * @return 사용자 프로필 응답 DTO
     * @author 전진
     * @DateOfCreated 2025-12-27
     * @DateOfEdit 2025-12-27
     */
    public UserProfileResponseDto getMyProfile(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return new UserProfileResponseDto(
                user.getUserId(),
                user.getUserEmail(),
                user.getNickName(),
                user.getPreferCategory(),
                user.getGender(),
                user.getAge(),
                user.getIntroduction()
        );
    }
    /**
     * 로그인한 사용자의 프로필 정보를 수정한다.
     *
     * userId(PK)를 기준으로 사용자 엔티티를 조회한 뒤,
     * 엔티티의 프로필 수정 메서드를 호출하여 상태를 변경한다.
     *
     * 트랜잭션 범위 내에서 변경 감지를 통해 DB에 반영된다.
     *
     * @param userId 수정 대상 사용자 PK
     * @param dto 사용자 프로필 수정 요청 DTO
     * @author 전진
     * @DateOfCreated 2025-12-29
     * @DateOfEdit 2025-12-29
     */
    @Transactional
    public void updateProfile(String userId, UserProfileUpdateRequestDto dto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        user.updateProfile(
                dto.getNickName(),
                dto.getPreferCategory(),
                dto.getIntroduction()
        );
    }


}
