package com.example.calendar.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT("C001", "잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("C002", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_DUPLICATED("C003", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("C004", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("C005", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("C006", "작성 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_ACTION("C007", "일정을 수정/삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SCHEDULE_NOT_FOUND("C008", "일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_LOGGED_IN("C009", "이미 로그인된 사용자가 있습니다.", HttpStatus.CONFLICT);



    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
