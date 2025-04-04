package com.example.calendar.controller;

import com.example.calendar.dto.*;
import com.example.calendar.exception.CustomException;
import com.example.calendar.exception.ErrorCode;
import com.example.calendar.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // 로깅을 위한 Logger 추가
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    /**
     * 일정 생성
     * @param scheduleRequestDto 사용자가 입력한 일정 정보 (작성자, 제목, 내용 등)
     * @param session 현재 로그인된 사용자 정보를 저장하는 세션
     * @return 생성된 일정 정보를 ScheduleResponseDto 형태로 반환
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto, HttpSession session) {
        // 1. 로그인 유저 확인 (세션에서 가져오기)
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 로그인되지 않은 경우 예외 발생
        }

        // 2. 로그인된 유저가 일정 작성자인지 확인
        if (!loggedInUser.getConsummerName().equals(scheduleRequestDto.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 작성자가 다르면 예외 발생
        }

        // 3. 일정 저장 후 생성된 일정 정보를 반환
        ScheduleResponseDto createdSchedule = scheduleService.saveSchedule(scheduleRequestDto);

        logger.info("일정 생성 완료: {}", createdSchedule); // 로그 기록
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

    /**
     * 일정 전체 조회
     * @return 모든 일정 정보를 List<ScheduleResponseDto> 형태로 반환
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll() {
        List<ScheduleResponseDto> allSchedulesList = scheduleService.findAllSchedules();
        logger.info("전체 일정 조회 요청: {}건", allSchedulesList.size()); // 로그 기록 - Consol 확인~
        return ResponseEntity.ok(allSchedulesList);
    }

    /**
     * 일정 단건 조회
     * @param id 조회할 일정 ID
     * @return 해당 ID의 일정 정보를 ScheduleResponseDto 형태로 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findByScheduleId(@PathVariable Long id) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.findByScheduleId(id);

        // 일정이 존재하지 않으면 예외 발생
        if (scheduleResponseDto == null) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        logger.info("일정 조회 성공: {}", scheduleResponseDto); // 로그 기록
        return ResponseEntity.ok(scheduleResponseDto);
    }


    /**
     * 일정 수정
     * @param id 수정할 일정 ID
     * @param updateRequest 일정 수정 요청 데이터 (제목, 내용 등)
     * @param session 현재 로그인된 사용자 정보를 저장하는 세션
     * @return 수정된 일정 정보를 ScheduleResponseDto 형태로 반환
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody UpdateScheduleRequestDto updateRequest,
            HttpSession session) {

        // 1. 로그인 유저 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 로그인 필요
        }

        // 2. 수정할 일정 가져오기
        ScheduleResponseDto existingSchedule = scheduleService.findByScheduleId(id);
        if (existingSchedule == null) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND); // 일정이 존재하지 않으면 예외 발생
        }

        // 3. 로그인한 유저와 일정 작성자 확인
        if (!loggedInUser.getConsummerName().equals(existingSchedule.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACTION); // 권한 없음
        }

        // 4. 일정 수정 수행
        ScheduleResponseDto updatedSchedule = scheduleService.updateSchedule(
                id,
                updateRequest.getScheduleWriter(),
                updateRequest.getScheduleTitle(),
                updateRequest.getScheduleContent(),
                updateRequest.getUserPassword()
        );

        logger.info("일정 수정 완료: {}", updatedSchedule); // 로그 기록
        return ResponseEntity.ok(updatedSchedule);
    }


    /**
     * 일정 삭제
     * @param id 삭제할 일정 ID
     * @param session 현재 로그인된 사용자 정보를 저장하는 세션
     * @return HTTP 상태 코드만 반환 (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpSession session) {
        // 1. 로그인된 유저 확인
        LoginResponseDto loggedInUser = (LoginResponseDto) session.getAttribute("LOGIN_USER");
        if (loggedInUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 로그인 필요
        }

        // 2. 삭제할 일정 가져오기 (중복 호출 방지)
        ScheduleResponseDto existingSchedule = scheduleService.findByScheduleId(id);
        if (existingSchedule == null) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND); // 일정이 존재하지 않음
        }

        // 3. 로그인한 유저와 일정 작성자 비교
        if (!loggedInUser.getConsummerName().equals(existingSchedule.getScheduleWriter())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACTION); // 권한 없음
        }

        // 4. 일정 삭제 처리
        scheduleService.deleteSchedule(id);

        logger.info("일정 삭제 완료: id={}", id); // 로그 기록
        return ResponseEntity.noContent().build(); // 삭제 후 204 응답 반환
    }

}