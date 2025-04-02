package com.example.calendar.dto;

import lombok.Getter;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
public class ConsummerRequestDto {

    private String consummerName;       // 사용자 이름

    private String consummerPassword;   // 사용자 패스워드

    private String consummerEmail;      // 사용자 이메일

}
