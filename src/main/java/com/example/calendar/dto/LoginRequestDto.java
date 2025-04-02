package com.example.calendar.dto;

import com.example.calendar.entity.Consummer;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String consummerEmail;
    private String consummerPassword;

}
