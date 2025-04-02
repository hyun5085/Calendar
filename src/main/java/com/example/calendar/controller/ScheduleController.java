package com.example.calendar.controller;

import com.example.calendar.dto.*;
import com.example.calendar.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    // 일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto, HttpSession session){

        // 로그인 유저 확인
        Object loginUser = session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Unauthorized
        }


        return new ResponseEntity<>(scheduleService.saveSchedule(scheduleRequestDto),HttpStatus.CREATED);
    }

    // 일정 전체 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(){
        List<ScheduleResponseDto> allSchedulesList = scheduleService.findAllSchedules();
        return new ResponseEntity<>(allSchedulesList, HttpStatus.OK);
    }

    // 일정 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findByScheduleId(@PathVariable Long id){
        ScheduleResponseDto scheduleResponseDto = scheduleService.findByScheduleId(id);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @RequestBody UpdateScheduleRequestDto updateRequest) {
        return new ResponseEntity<>(
                scheduleService.updateSchedule(
                        id,
                        updateRequest.getScheduleWriter(),
                        updateRequest.getScheduleTitle(),
                        updateRequest.getScheduleContent(),
                        updateRequest.getUserPassword() //
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
