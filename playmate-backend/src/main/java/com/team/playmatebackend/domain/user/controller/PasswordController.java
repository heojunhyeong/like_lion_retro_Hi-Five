package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordController {

    private final UserService userService;

    @PostMapping("/reset-request")
    public void requestReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
    }

    @PostMapping("/reset")
    public void resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
    }
}
