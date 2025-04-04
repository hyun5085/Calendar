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

@RestController
@RequestMapping("/consummers")
@RequiredArgsConstructor
public class LoginController {

    private final ConsummerService consummerService;

    /**
     * 로그인 처리
     * @param loginRequestDto 로그인 요청 데이터 (이메일, 비밀번호)
     * @param request HTTP 요청 객체 (세션 사용을 위해 필요)
     * @return 로그인 성공 시 사용자 정보 반환 (ResponseEntity<LoginResponseDto>)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        // 세션 가져오기
        HttpSession session = request.getSession();

        // 로그인 시도
        Consummer loginUser = consummerService.login(
                loginRequestDto.getConsummerEmail(),
                loginRequestDto.getConsummerPassword()
        );

        // 로그인 실패 시 예외 발생
        if (loginUser == null) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인된 사용자 정보를 DTO로 변환
        LoginResponseDto responseDto = new LoginResponseDto(loginUser);

        // 세션에 로그인 정보를 저장
        session.setAttribute("LOGIN_USER", responseDto);

        // 디버깅용 출력문 (나중에 제거해도 됨)
        System.out.println("로그인 성공! 세션 저장: " + session.getAttribute("LOGIN_USER"));

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 로그아웃 처리
     * - 서버 세션 무효화
     * - 클라이언트의 JSESSIONID 쿠키 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 서버에서 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 삭제
        }

        // 2. JSESSIONID 쿠키 삭제 (보안 옵션 추가)
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0); // 즉시 삭제
        cookie.setPath("/"); // 모든 경로에서 삭제
        cookie.setHttpOnly(true); // JS에서 접근 불가 (보안 강화)
        cookie.setSecure(true); // HTTPS에서만 전송 (보안 강화)
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
