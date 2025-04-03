package com.example.calendar.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorcode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorcode = errorCode;

    }

}
