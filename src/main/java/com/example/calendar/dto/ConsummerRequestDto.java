package com.example.calendar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
public class ConsummerRequestDto {

    private String consummerName;       // 사용자 이름

    private String consummerPassword;   // 사용자 패스워드

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "올바른 이메일 형식이 아닙니다.")
    private String consummerEmail;      // 사용자 이메일

}
