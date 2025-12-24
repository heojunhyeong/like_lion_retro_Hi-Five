package com.team.playmatebackend.domain.user.service;
/**
 * 역할 분리를 위한 MailService
 *
 * @author 허준형
 * @DateOfCreated 2025-12-24
 * @DateOfEdit 2025-12-24
 */
public interface MailService {
    void sendPasswordResetMail(String email, String resetLink);
}
