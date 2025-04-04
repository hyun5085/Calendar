package com.example.calendar.controller;

import com.example.calendar.exception.CustomException;
import com.example.calendar.exception.ErrorCode;
import com.example.calendar.dto.LoginRequestDto;
import com.example.calendar.dto.LoginResponseDto;
import com.example.calendar.entity.Consummer;
import com.example.calendar.service.ConsummerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 로그인 처리
     * @param loginRequestDto 로그인 요청 데이터 (이메일, 비밀번호)
     * @param request HTTP 요청 객체 (세션 사용을 위해 필요)
     * @return 로그인 성공 시 사용자 정보 반환 (ResponseEntity<LoginResponseDto>)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login( @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        // 세션 가져오기
        HttpSession session = request.getSession();

        // 로그인 시도
        Consummer loginUser = consummerService.login(
                loginRequestDto.getConsummerEmail(),
                loginRequestDto.getConsummerPassword()
        );

        if (loginUser == null) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }


        // 로그인된 사용자 정보를 DTO로 변환
        LoginResponseDto responseDto = new LoginResponseDto(loginUser);

        // 세션에 로그인 정보를 저장
        session.setAttribute("LOGIN_USER", responseDto);

        // 디버깅용 출력문
        System.out.println("로그인 성공! 세션 저장: " + session.getAttribute("LOGIN_USER"));

        return ResponseEntity.ok(responseDto);
    }


    // 로그아웃 처리
    // 클라이언트의 세션을 무효화하고 로그인 정보를 삭제
    // JSESSIONID 쿠키를 제거하여 클라이언트 측에서도 세션을 무효화
    // 로그아웃 후에는 새로운 로그인 없이 보호된 API에 접근할 수 없음
    // 요청 데이터: 없음
    // 응답 데이터: ResponseEntity<Void> (응답 본문 없이 상태 코드만 반환)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 세션 무효화 (로그인 정보 삭제)
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 세션을 무효화하여 로그아웃 처리
            session.invalidate();
        }

        // 2. 쿠키 삭제 (클라이언트에서 세션 정보 제거)
        Cookie cookie = new Cookie("JSESSIONID", null);  // JSESSIONID는 기본 세션 쿠키
        cookie.setMaxAge(0); // 쿠키 만료 설정
        cookie.setPath("/"); // 모든 경로에서 삭제되도록 설정
        response.addCookie(cookie);

        // 3. 응답 반환
        return ResponseEntity.ok().build();
    }



}
