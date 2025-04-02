package com.example.calendar.dto;

import com.example.calendar.entity.Consummer;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private String consummerName;
    private String consummerEmail;

    public LoginResponseDto(Consummer consummer) {
        this.consummerName = consummer.getConsummerName();
        this.consummerEmail = consummer.getConsummerEmail();
    }

    public LoginResponseDto(String consummerName, String consummerEmail) {
        this.consummerName = consummerName;
        this.consummerEmail = consummerEmail;
    }
}
