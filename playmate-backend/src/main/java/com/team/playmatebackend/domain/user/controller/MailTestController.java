package com.team.playmatebackend.domain.user.controller;

import com.team.playmatebackend.domain.user.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러
 * @author 허준형
 * @DateOfCreated 2025-12-26
 * @DateOfEdit 2025-12-26
 */
@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final MailService mailService;

    @GetMapping("/test/mail")
    public String sendTestMail(@RequestParam String email) {

        String testLink = "http://localhost:8080/reset-password?token=TEST_TOKEN";

        mailService.sendPasswordResetMail(email, testLink);

        return "메일 전송 완료";
    }
}