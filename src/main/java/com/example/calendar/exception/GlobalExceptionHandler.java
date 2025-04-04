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

@RestControllerAdvice // 전역 예외 처리 클래스: 모든 컨트롤러에서 발생한 예외를 처리하고 적절한 응답을 반환
// // @RestControllerAdvice는 @ControllerAdvice와 @ResponseBody의 조합으로, 예외 발생 시 JSON 형식의 응답을 자동으로 반환
public class GlobalExceptionHandler {

    // CustomException이 발생하면 이 메서드가 실행
    // CustomException에 정의된 errorCode 정보를 기반으로 HTTP 상태 코드와 메시지를 클라이언트에게 전달
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {

        // 발생한 CustomException에서 ErrorCode를 가져옵니다
        ErrorCode errorCode = ex.getErrorcode();

        // ErrorResponseDto 객체를 생성하여 ErrorCode에 맞는 응답을 구성합니다.
        ErrorResponseDto errorResponse = new ErrorResponseDto( // 예외에서 ErrorCode를 가져와서 ErrorResponseDto로 변환
                errorCode.getCode(),        // 오류 코드
                errorCode.getMessage(),     // 오류 메시지
                errorCode.getStatus()       // HTTP 상태 코드
        );
        // ResponseEntity로 적절한 HTTP 상태 코드와 함께 반환
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    // @Valid 검증 실패 시 발생하는 MethodArgumentNotValidException을 처리하는 메서드
    // 주로 폼 데이터를 검증할 때 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // 응답을 담을 Map 생성
        Map<String, Object> response = new HashMap<>();

        // 응답에 타임스탬프와 상태 코드, 오류 메시지를 포함합니다.
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");

        // 필드별로 발생한 에러 메시지를 수집하여 "errors"에 담습니다.
        Map<String, String> errors = new HashMap<>();

        // 검증 실패한 필드들을 순차적으로 처리
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

            // 필드 이름과 해당 필드에 대한 오류 메시지를 추가
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // 최종 응답에 필드별 에러 메시지를 포함시킵니다.
        response.put("errors", errors);

        // 400 Bad Request 상태 코드와 함께 오류 응답을 반환합니다.
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
