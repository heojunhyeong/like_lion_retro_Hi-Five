package com.team.playmatebackend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 패스워드 재설정 메일 발송 메서드
 * spring-starter-mail 프레임워크 사용
 *
 * @author 허준형
 * @DateOfCreated 2025-12-26
 * @DateOfEdit 2025-12-26
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetMail(String email, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("[PlayMate] 비밀번호 재설정 안내");
        message.setText(
                "비밀번호 재설정을 요청하셨습니다.\n\n" +
                        "아래 링크를 클릭하여 비밀번호를 재설정해주세요.\n\n" +
                        resetLink + "\n\n" +
                        "해당 링크는 30분간 유효합니다."
        );

        mailSender.send(message);
    }
}
