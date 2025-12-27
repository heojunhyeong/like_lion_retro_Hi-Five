package com.team.playmatebackend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 예외가 컨트롤러 밖으로 던져지면 해당 클래스가 낚아챔
 * CustomException이 발생하면 handelCustomException가 실행되고
 * 그 외 예외가 발생되면 handleException 실행됨
 *
 * @author 허준형
 * @DateOfCreated 2025-12-27
 * @DateOfEdit 2025-12-27
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}