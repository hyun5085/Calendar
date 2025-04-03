package com.example.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 전역 예외 처리용 클래스임을 선언
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // CustomException이 발생하면 이 메서드가 실행됨
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {

        ErrorCode errorCode = ex.getErrorcode();

        ErrorResponseDto errorResponse = new ErrorResponseDto( // 예외에서 ErrorCode를 가져와서 ErrorResponseDto로 변환
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus()
        );
        // ResponseEntity로 적절한 HTTP 상태 코드와 함께 반환
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    // @Valid 검증 실패 시 발생하는 MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");

        // 필드별 에러 메시지 추가
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
