package com.team.playmatebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 하트비트 기능을 위한 EnableScheduling 어노테이션 추가
 *
 * @author 김지번
 * @DateOfEdit 2026-01-04
 */

@EnableScheduling
@SpringBootApplication
public class PlaymateBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaymateBackendApplication.class, args);
    }

}
