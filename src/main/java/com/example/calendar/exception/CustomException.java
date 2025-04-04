package com.example.calendar.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException{


    private final ErrorCode errorcode; // ErrorCode : 예) INVALID_INPUT("C001", "잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),

    // 생성자: ErrorCode를 받아 CustomException을 생성
    // errorCode에 해당하는 메시지를 부모 클래스(RuntimeException)에 전달하고,
    // errorCode 값을 필드에 저장합니다.
    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorcode = errorCode;

    }

}
