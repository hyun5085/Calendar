package com.example.calendar.controller;

import com.example.calendar.exception.CustomException;
import com.example.calendar.exception.ErrorCode;
import com.example.calendar.dto.LoginRequestDto;
import com.example.calendar.dto.LoginResponseDto;
import com.example.calendar.entity.Consummer;
import com.example.calendar.service.ConsummerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicBoolean;


@RestController
@RequestMapping("/consummers")
@RequiredArgsConstructor
public class LoginController {

    private final ConsummerService consummerService;

    // 🔥 현재 로그인 상태를 관리하는 변수 (true: 로그인됨, false: 로그인 없음)
    private static final AtomicBoolean isLoggedIn = new AtomicBoolean(false);

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {

        // 1. 로그인된 사용자가 있는지 확인
        if (isLoggedIn.get()) {
            throw new CustomException(ErrorCode.ALREADY_LOGGED_IN);
        }

        // 2. 로그인 시도
        Consummer loginUser = consummerService.login(
                loginRequestDto.getConsummerEmail(),
                loginRequestDto.getConsummerPassword()
        );

        if (loginUser == null) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. 로그인 성공 → 상태 변경 (true: 로그인됨, false: 로그인 없음)
        isLoggedIn.set(true);

        // 4. 세션에 로그인 정보 저장
        LoginResponseDto responseDto = new LoginResponseDto(
                loginUser.getConsummerName(),
                loginUser.getConsummerEmail()
        );

        session.setAttribute("LOGIN_USER", responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        // 1. 현재 세션이 존재하면 삭제
        session.invalidate();

        // 2. 전역 로그인 상태 초기화
        isLoggedIn.set(false);

        return ResponseEntity.ok().build();
    }

}
