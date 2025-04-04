package com.example.calendar.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j       // @Slf4j : 로그 객체를 생성해줍니다.
public class LoginFilter implements Filter {

    // 화이트리스트에 포함된 URL은 로그인 검사를 하지 않음
    private static String[] WHITE_LIST = {"/", "/consummers", "/login", "/logout"};


    // HTTP 요청을 필터링하고, 로그인 상태를 확인하는 메서드
    // 요청 데이터 형식: ServletRequest, ServletResponse (각각 HTTP 요청과 응답 객체)
    // 반환 데이터 형식: 없음 (필터 체인으로 요청을 넘깁니다)
    // @throws IOException: 입출력 오류 발생 시
    // @throws ServletException: 서블릿 관련 오류 발생 시
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 1. HTTP 요청을 가져옵니다. (ServletRequest -> HttpServletRequest로 캐스팅)
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // 2. 요청 URI를 가져옵니다. (어떤 경로로 요청이 들어왔는지 확인)
        String requestURI = httpRequest.getRequestURI();

        // 3. HTTP 응답을 가져옵니다. (ServletResponse -> HttpServletResponse로 캐스팅)
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 4. 로그를 찍어 필터가 실행됨을 확인합니다.
        log.info("로그인 필터 로직 실행");

        // 5. 요청 URI가 화이트리스트에 없으면 로그인 여부를 확인합니다.
        if (!isWhiteList(requestURI)){
            // 6. 세션을 가져옵니다. (세션이 없으면 로그인하지 않은 상태로 판단)
            HttpSession session = httpRequest.getSession(false);

            // 7. 세션이 없거나 "LOGIN_USER"가 없으면 로그인하지 않은 상태로 간주
            if(session == null || session.getAttribute("LOGIN_USER") == null){

                // 8. 로그인하지 않은 상태라면 예외를 던집니다.
                throw new RuntimeException("로그인 해주세요.");
            }

            // 9. 로그인 상태라면 성공 로그를 찍습니다.
            log.info("로그인에 성공했습니다.");

        }

        // 10. 필터 체인을 통해 요청을 다음 필터나 서블릿으로 전달합니다.
        filterChain.doFilter(httpRequest, httpResponse);

    }

    // 11. 화이트리스트에 해당하는 URI인지 확인하는 메서드
    private boolean isWhiteList(String requestURI){
        // 요청 URI가 화이트리스트 배열에 포함되면 true 반환
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
