package com.example.calendar.service;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.dto.LoginRequestDto;
import com.example.calendar.dto.LoginResponseDto;
import com.example.calendar.entity.Consummer;

import java.util.List;

public interface ConsummerService {

    ConsummerResponseDto saveConsummer(ConsummerRequestDto consummerRequestDto);

    ConsummerResponseDto findByConsummerId(Long id);

    List<ConsummerResponseDto> findAllConsummer();

    void updatePassword(Long id, String oldPassword, String newPassword);

    void deleteConsummer(Long id, String consummerPassword);

    // 로그인
    Consummer login(String email, String password);
}
