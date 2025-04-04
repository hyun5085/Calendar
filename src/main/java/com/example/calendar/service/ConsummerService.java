package com.example.calendar.service;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.entity.Consummer;

import java.util.List;

public interface ConsummerService {

    // 유저 등록
    // 유저 정보(ConsummerRequestDto)를 받아 새로운 유저 정보를 저장하고, 저장된 유저 정보를 ConsummerResponseDto 형식으로 반환
    // 요청 데이터 형식: ConsummerRequestDto
    // 반환 데이터 형식: ConsummerResponseDto
    ConsummerResponseDto saveConsummer(ConsummerRequestDto consummerRequestDto);

    // 유저 조회 (단건)
    // 유저 ID를 받아 해당 유저의 정보를 조회하고, 조회된 유저 정보를 ConsummerResponseDto 형식으로 반환
    // 요청 데이터 형식: Long id
    // 반환 데이터 형식: ConsummerResponseDto
    ConsummerResponseDto findByConsummerId(Long id);

    // 유저 조회 (전체)
    // 전체 유저 목록을 조회하고, 조회된 유저 정보를 List<ConsummerResponseDto> 형식으로 반환
    // 반환 데이터 형식: List<ConsummerResponseDto> (전체 유저 정보)
    List<ConsummerResponseDto> findAllConsummer();

    // 패스워드 수정
    // 유저의 패스워드를 수정하는 기능. 기존 패스워드와 새로운 패스워드를 받아서 패스워드를 변경
    // 요청 데이터 형식: Long id (유저 ID), String oldPassword (기존 패스워드), String newPassword (새로운 패스워드)
    void updatePassword(Long id, String oldPassword, String newPassword);

    // 유저 삭제
    // ID와 패스워드를 받아 해당 유저 정보를 삭제
    // 요청 데이터 형식: Long id (유저 ID), String consummerPassword (유저 패스워드)
    // 반환 데이터 형식: 없음 (성공 시 void)
    void deleteConsummer(Long id, String consummerPassword);

    // 로그인
    // 이메일과 비밀번호를 받아 로그인 시 인증을 수행. 로그인 성공 시 해당 유저 정보를 반환
    // 요청 데이터 형식: String email (이메일), String password (비밀번호)
    // 반환 데이터 형식: Consummer (로그인한 유저 정보)
    Consummer login(String email, String password);

    // 🔹 유저 비밀번호 검증 메서드 추가
    boolean validateConsummerPassword(Long id, String password);
}
