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
// @Controller와 @ResponseBody 어노테이션의 조합
// @Controller: 일반적인 Spring MVC 컨트롤러를 정의
// @ResponseBody: 메서드의 반환값을 HTTP 응답 본문에 직접 매핑하도록 지정
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    // ScheduleService는 일정과 관련된 비즈니스 로직을 처리하는 서비스 클래스
    // 이 클래스는 일정 생성, 조회, 수정, 삭제 등의 기능을 담당
    // Spring의 의존성 주입(DI)을 통해 이 서비스 클래스의 인스턴스를 자동으로 주입
    private final ScheduleService scheduleService;
    // 일정 생성

    // 일정 생성
    // HTTP 요청을 처리하고, 새로 생성된 일정 정보를 ScheduleResponseDto 객체와 함께 반환하는 메서드
    // 요청 데이터 형식: ScheduleRequestDto (JSON 본문에서 매핑됨)
    // 반환 데이터 형식: ResponseEntity<ScheduleResponseDto> (응답 데이터 + HTTP 상태 코드 포함)
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto, HttpSession session){


        // 로그인 유저 확인
        // 세션에서 로그인 정보를 가져와 로그인된 사용자가 있는지 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            // 로그인된 유저가 없으면 예외 발생
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 로그인 유저(consummerName)와 작성자명(scheduleWriter) 비교
        if (!loggedInUser.getConsummerName().equals(scheduleRequestDto.getScheduleWriter())) {
            // 작성자가 다르면 예외 발생
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 일정 저장 요청 처리 후, 생성된 일정 정보를 응답으로 반환
        return new ResponseEntity<>(scheduleService.saveSchedule(scheduleRequestDto),HttpStatus.CREATED);
    }

    // 일정 전체 조회
    // HTTP 요청을 처리하고, 모든 일정 정보를 ScheduleResponseDto 형태로 반환하는 메서드
    // 요청 데이터 형식: 없음 (전체 일정 목록 조회)
    // 반환 데이터 형식: ResponseEntity<List<ScheduleResponseDto>> (응답 데이터 + HTTP 상태 코드 포함)
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(){

        // 전체 일정 목록 조회
        List<ScheduleResponseDto> allSchedulesList = scheduleService.findAllSchedules();

        // 저장된 일정 목록을 반환
        return new ResponseEntity<>(allSchedulesList, HttpStatus.OK);
    }

    // 일정 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findByScheduleId(@PathVariable Long id){
        ScheduleResponseDto scheduleResponseDto = scheduleService.findByScheduleId(id);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }


    // 일정 수정
    // 일정 단건 조회
    // HTTP 요청을 처리하고, 특정 일정 정보를 ScheduleResponseDto 형태로 반환하는 메서드
    // 요청 데이터 형식: URL PathVariable(Long id)
    // 반환 데이터 형식: ResponseEntity<ScheduleResponseDto> (응답 데이터 + HTTP 상태 코드 포함)
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id,
                                                              @RequestBody UpdateScheduleRequestDto updateRequest,
                                                              HttpSession session) {

        // 로그인 유저 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");

        // 세션에서 로그인 정보를 가져와 로그인된 사용자가 있는지 확인
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

        // 일정 수정 처리 후, 수정된 일정 정보를 응답으로 반환
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


    // 일정 삭제
    // HTTP 요청을 처리하고, 일정 삭제 후 HTTP 상태 코드를 반환하는 메서드
    // 요청 데이터 형식: URL PathVariable(Long id), ConsummerRequestDto (삭제할 일정에 대한 패스워드 정보)
    // 반환 데이터 형식: ResponseEntity<Void> (응답 데이터 없음, 상태 코드만 반환)
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpSession session){

        // 로그인된 유저 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");

        // 세션에서 로그인 정보를 가져와 로그인된 사용자가 있는지 확인
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 2. 삭제하려는 일정 정보 가져오기
        ScheduleResponseDto existingSchedule = scheduleService.findByScheduleId(id);
        if (existingSchedule == null) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        // 3. 로그인한 유저와 일정 작성자 비교
        String scheduleWriter = existingSchedule.getScheduleWriter(); // 작성자 정보
        String loggedInUserName = loggedInUser.getConsummerName(); // 로그인한 유저 이름

        // 작성자가 다르면 예외 발생
        if (!loggedInUserName.trim().equals(scheduleWriter.trim())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACTION);
        }

        // 일정 삭제 요청 처리
        scheduleService.deleteSchedule(id);

        // 삭제 완료 후 상태 코드 200 OK 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
