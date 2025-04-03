package com.example.calendar.controller;

import com.example.calendar.dto.*;
import com.example.calendar.exception.CustomException;
import com.example.calendar.exception.ErrorCode;
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
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 로그인 유저(consummerName)와 작성자명(scheduleWriter) 비교
        if (!loggedInUser.getConsummerName().equals(scheduleRequestDto.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
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


    // 일정 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @RequestBody UpdateScheduleRequestDto updateRequest,
                                                              HttpSession session) {

        // 로그인 유저 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 로그인 유저(consummerName)와 작성자명(scheduleWriter) 비교
        if (!loggedInUser.getConsummerName().equals(updateRequest.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 기존 일정 정보 가져오기
        ScheduleResponseDto existingSchedule = scheduleService.findByScheduleId(id);

        // 로그인한 유저와 작성자가 다르면 예외 발생
        if (!loggedInUser.getConsummerName().equals(existingSchedule.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACTION);
        }


        return new ResponseEntity<>(
                scheduleService.updateSchedule(
                        id,
                        updateRequest.getScheduleWriter(),
                        updateRequest.getScheduleTitle(),
                        updateRequest.getScheduleContent(),
                        updateRequest.getUserPassword()
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpSession session){

        // 1️⃣ 로그인한 유저 정보 가져오기
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 2️⃣ 삭제하려는 일정 정보 가져오기
        ScheduleResponseDto existingSchedule = scheduleService.findByScheduleId(id);
        if (existingSchedule == null) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        // 3️⃣ 로그인한 유저와 일정 작성자 비교
        String scheduleWriter = existingSchedule.getScheduleWriter(); // 작성자 정보
        String loggedInUserName = loggedInUser.getConsummerName(); // 로그인한 유저 이름

        if (!loggedInUserName.trim().equals(scheduleWriter.trim())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACTION);
        }

        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
