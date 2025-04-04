package com.example.calendar.service;

import com.example.calendar.dto.ScheduleRequestDto;
import com.example.calendar.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    // 일정 생성
    // 일정 생성 요청을 처리하고, 요청에 따라 새 일정을 저장 후 응답 반환
    // 요청 데이터 형식: ScheduleRequestDto (일정 제목, 내용, 작성자 등)
    // 반환 데이터 형식: ScheduleResponseDto (저장된 일정 정보)
    ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);

    // 전체 일정 조회
    // 모든 일정을 조회하여 반환
    // 반환 데이터 형식: List<ScheduleResponseDto> (전체 일정 목록)
    List<ScheduleResponseDto> findAllSchedules();

    // 일정 단건 조회
    // 특정 ID를 가진 일정을 조회하고, 해당 일정의 정보를 반환
    // 요청 데이터 형식: Long id (일정 ID)
    // 반환 데이터 형식: ScheduleResponseDto (조회된 일정 정보)
    ScheduleResponseDto findByScheduleId(Long id);

    // 일정 수정
    // 특정 ID를 가진 일정을 수정하고, 수정된 일정 정보를 반환
    // 요청 데이터 형식: Long id (일정 ID), String scheduleWriter (작성자), String scheduleTitle (제목), String scheduleContent (내용), String schedulePassword (패스워드)
    // 반환 데이터 형식: ScheduleResponseDto (수정된 일정 정보)
    ScheduleResponseDto updateSchedule(Long id, String scheduleWriter, String scheduleTitle, String scheduleContent, String schedulePassword);

    // 일정 삭제
    // 특정 ID를 가진 일정을 삭제
    // 요청 데이터 형식: Long id (일정 ID)
    // 반환 데이터 형식: 없음 (삭제된 일정은 응답에 포함되지 않음)
    void deleteSchedule(Long id);




}
