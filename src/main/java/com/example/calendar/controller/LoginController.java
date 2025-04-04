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

    // 현재 로그인 상태를 관리하는 변수 (true: 로그인됨, false: 로그인 없음)
    private static final AtomicBoolean isLoggedIn = new AtomicBoolean(false);


    // 로그인 처리
    // HTTP 요청을 처리하고, 로그인 성공 시 사용자 정보를 포함한 응답을 반환하는 메서드
    // 요청 데이터 형식: LoginRequestDto (JSON 본문에서 매핑됨)
    // 반환 데이터 형식: ResponseEntity<LoginResponseDto> (응답 데이터 + HTTP 상태 코드 포함)
    // @ResponseBody: 메서드의 반환값을 HTTP 응답 본문에 직접 매핑하도록 지정
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {

        // 1. 이미 로그인된 사용자가 있는지 확인
        // 로그인 상태가 true라면 이미 로그인된 상태이므로 예외 처리
        if (isLoggedIn.get()) {
            throw new CustomException(ErrorCode.ALREADY_LOGGED_IN);
        }

        // 2. 로그인 시도
        // 서비스에서 이메일과 패스워드를 이용해 로그인 처리 후, 사용자가 유효한지 확인
        Consummer loginUser = consummerService.login(
                loginRequestDto.getConsummerEmail(),
                loginRequestDto.getConsummerPassword()
        );

        // 3. 로그인 실패: 잘못된 패스워드인 경우 예외 처리
        if (loginUser == null) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 4. 로그인 성공 → 상태 변경 (true: 로그인됨, false: 로그인 없음)
        // 로그인 성공 시 전역 로그인 상태를 true로 설정
        isLoggedIn.set(true);

        // 5. 세션에 로그인 정보 저장
        // 로그인 성공한 사용자의 정보를 LoginResponseDto로 반환하고, 이를 세션에 저장
        LoginResponseDto responseDto = new LoginResponseDto(
                loginUser.getConsummerName(),
                loginUser.getConsummerEmail()
        );

        // 세션에 로그인 정보 저장
        // 세션에 "LOGIN_USER"라는 키로 로그인한 사용자의 정보를 저장
        // 이 정보는 이후 다른 요청에서 세션을 통해 사용자 정보를 사용할 수 있도록 함
        session.setAttribute("LOGIN_USER", responseDto);

        // 로그인 성공 시, 응답으로 사용자 정보를 반환
        return ResponseEntity.ok(responseDto);
    }

    // 로그 아웃 처리
    // HTTP 요청을 처리하고, 로그 아웃 시 세션을 무효화하고 로그인 상태를 초기화하는 메서드
    // 요청 데이터 형식: 없음 (로그 아웃은 요청 본문 없이 처리)
    // 반환 데이터 형식: ResponseEntity<Void> (응답 데이터 없음, 상태 코드만 반환)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {

        // 1. 현재 세션이 존재하면 삭제
        // 세션을 무효화하여 사용자 정보를 제거
        session.invalidate();

        // 2. 전역 로그인 상태 초기화
        // 로그 아웃 후 전역 로그인 상태를 false로 설정
        isLoggedIn.set(false);

        // 로그 아웃 성공 후, 상태 코드 200 OK를 반환 (응답 본문 없음)
        return ResponseEntity.ok().build();
    }

}
