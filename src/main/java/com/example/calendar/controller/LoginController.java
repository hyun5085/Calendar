package com.example.calendar.controller;

import com.example.calendar.dto.LoginRequestDto;
import com.example.calendar.dto.LoginResponseDto;
import com.example.calendar.entity.Consummer;
import com.example.calendar.service.ConsummerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consummers")
@RequiredArgsConstructor
public class LoginController {

    private final ConsummerService consummerService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpSession session
    ) {
        // 로그인 검증
        Consummer loginUser = consummerService.login(
                loginRequestDto.getConsummerEmail(),
                loginRequestDto.getConsummerPassword()
        );

        // 세션에 로그인 정보 저장
        LoginResponseDto responseDto = new LoginResponseDto(
                loginUser.getConsummerName(),
                loginUser.getConsummerEmail()
        );

        session.setAttribute("LOGIN_USER", responseDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        // 현재 세션이 존재하면 삭제
        session.invalidate();
        return ResponseEntity.ok().build();
    }

}
