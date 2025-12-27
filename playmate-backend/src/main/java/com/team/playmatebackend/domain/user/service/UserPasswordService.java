//package com.team.playmatebackend.domain.user.service;
//
//import com.team.playmatebackend.domain.user.entity.User;
//import com.team.playmatebackend.domain.user.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//public class UserPasswordService {
//    private final UserRepository userRepository;
//    @Transactional
//    public void changePassword(Long userId, PasswordChangeRequestDto dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow();
//
//        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getUserPassword())) {
//            throw new IllegalArgumentException("비밀번호 불일치");
//        }
//
//        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
//    }
//}
