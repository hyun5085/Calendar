package com.example.calendar.service;

import com.example.calendar.dto.ScheduleRequestDto;
import com.example.calendar.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);

    List<ScheduleResponseDto> findAllSchedules();

    ScheduleResponseDto findByScheduleId(Long id);

    ScheduleResponseDto updateSchedule(Long id, String scheduleWriter, String scheduleTitle, String scheduleContent, String schedulePassword);

    void deleteSchedule(Long id);




}
