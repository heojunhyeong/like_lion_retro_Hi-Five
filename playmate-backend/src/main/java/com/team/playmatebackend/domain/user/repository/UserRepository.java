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

    Optional<User> findByUserEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

// 로그인시 userId만 사용해서 로그인 하는 것이 아닌 userEmail로도 로그인 가능하게 만드는 쿼리
// Optional<User> findByUserIdOrUserEmail(String userId, String userEmail);
// User user = userRepository
//       .findByUserIdOrUserEmail(request.getLoginId(), request.getLoginId())
//       .orElseThrow(() -> new IllegalArgumentException("계정 없음"));
// UserServiceImpl에 수정하면 좋을 것 같긴한데 복잡할 것 같아서 일단 남겨둠 - 허준형

}