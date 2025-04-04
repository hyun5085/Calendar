package com.example.calendar.dto;

import com.example.calendar.entity.Consummer;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private String consummerName;   // 유저 이름
    private String consummerEmail;  // 작성자 Email

    public LoginResponseDto(Consummer consummer) {
        this.consummerName = consummer.getConsummerName();
        this.consummerEmail = consummer.getConsummerEmail();
    }

    public LoginResponseDto(String consummerName, String consummerEmail) {
        this.consummerName = consummerName;
        this.consummerEmail = consummerEmail;
    }
}
