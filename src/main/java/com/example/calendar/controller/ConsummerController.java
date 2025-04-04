package com.example.calendar.controller;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.dto.UpdatePasswordRequestDto;
import com.example.calendar.service.ConsummerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @Controller와 @ResponseBody 어노테이션의 조합
// @Controller: 일반적인 Spring MVC 컨트롤러를 정의
// @ResponseBody: 메서드의 반환값을 HTTP 응답 본문에 직접 매핑하도록 지정
@RequestMapping("/consummers")
@RequiredArgsConstructor // 필수 생성자를 자동으로 생성해주는 기능
public class ConsummerController {          // Controller 는 적절한 서비스를 호출한 뒤 응답을 반환함.

    private final ConsummerService consummerService;

    // 유저 생성
    // HTTP 요청을 처리하고, ConsummerResponseDto 객체와 HTTP 상태 코드를 함께 반환하는 메서드
    // 요청 데이터 형식: ConsummerRequestDto (JSON 본문에서 매핑됨)
    // 반환 데이터 형식: ResponseEntity<ConsummerResponseDto> (응답 데이터 + HTTP 상태 코드 포함)
    // @RequestBody  HTTP 요청의 본문(body)에 있는 JSON 데이터를 ConsummerRequestDto 객체로 변환
    // @Valid : ConsummerRequestDto에 정의된 @NotBlank, @Email 같은 유효성 검증(Validation)을 수행
    @PostMapping("/signup")
    public ResponseEntity<ConsummerResponseDto> saveConsummer(@RequestBody @Valid ConsummerRequestDto consummerRequestDto){

        // 반환은 Http 응답으로 표현 : 회원 저장 요청을 처리하는 비즈니스 로직을 수행 하고 회원 정보를 응답으로 반환
        // ResponseEntity <>() : 응답 데이터와 함께 Http 상태 코드를 포함하여 반환 ( 응답 데이터는 필수가 아님 )
        return new ResponseEntity<>(consummerService.saveConsummer(consummerRequestDto),HttpStatus.CREATED);

    }

    // 유저 단건 조회
    // HTTP 요청을 처리하고, ConsummerResponseDto 객체와 HTTP 상태 코드를 함께 반환하는 메서드
    // 요청 데이터 형식: URL PathVariable(Long id)
    // 반환 데이터 형식: ResponseEntity<ConsummerResponseDto> (응답 데이터 + HTTP 상태 코드 포함)
    // @PathVariable: 요청 URL의 {id} 값을 메서드 파라미터(Long id)로 매핑
    @GetMapping("/{id}")
    public ResponseEntity<ConsummerResponseDto> findByConsummerId(@PathVariable Long id){

        // consummerService에서 소비자 정보를 조회하는 메서드를 호출
        // Long id 를 이용해 데이터베이스에서 해당 소비자 정보를 조회하고, 그 결과를 consummerResponseDto에 저장
        // consummerResponseDto는 메서드 내에서만 사용되는 지역 변수로, 다른 변수와 이름이 겹치지 않음
        ConsummerResponseDto consummerResponseDto = consummerService.findByConsummerId(id);

        // 조회된 소비자 정보를 응답으로 반환
        return new ResponseEntity<>(consummerResponseDto, HttpStatus.OK);
    }

    // 유저 전체 조회
    // HTTP 요청을 처리하고, List<ConsummerResponseDto> 객체와 HTTP 상태 코드를 함께 반환하는 메서드
    // 요청 데이터 형식: 없음 (전체 유저 목록 조회)
    // 반환 데이터 형식: ResponseEntity<List<ConsummerResponseDto>> (응답 데이터 + HTTP 상태 코드 포함)
    @GetMapping
    public ResponseEntity<List<ConsummerResponseDto>> findAllConsummer(){

        // consummerService에서 전체 소비자 정보를 조회하는 메서드를 호출
        // 전체 소비자 정보를 반환받아 allConsummer에 저장
        List<ConsummerResponseDto> allConsummer = consummerService.findAllConsummer();

        // 조회된 소비자 List를 응답으로 반환
        return new ResponseEntity<>(allConsummer, HttpStatus.OK);
    }

    // 유저 패스워드 수정
    // HTTP 요청을 처리하고, 패스워드 수정 후 HTTP 상태 코드를 반환하는 메서드
    // 요청 데이터 형식: URL PathVariable(Long id), 요청 본문에 포함된 UpdatePasswordRequestDto (새로운 패스워드 정보)
    // 반환 데이터 형식: ResponseEntity<Void> (응답 데이터 없음, 상태 코드만 반환)
    // @PathVariable: 요청 URL의 {id} 값을 메서드 파라미터(Long id)로 매핑
    @PatchMapping("/{id}")
    public ResponseEntity<Void> consummerUpdatePassword(@PathVariable Long id,
                                                        @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto){

        // consummerService에서 패스워드를 수정하는 메서드를 호출
        // 기존 패스워드와 새로운 패스워드를 전달하여 소비자 패스워드를 업데이트
        consummerService.updatePassword(
                id,
                updatePasswordRequestDto.getOldPassword(),
                updatePasswordRequestDto.getNewPassword()
        );

        // 패스워드 수정 성공 후, 상태 코드 200 OK를 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 유저 삭제
    // HTTP 요청을 처리하고, 유저 삭제 후 HTTP 상태 코드를 반환하는 메서드
    // 요청 데이터 형식: URL PathVariable(Long id), 요청 본문에 포함된 ConsummerRequestDto (삭제할 소비자의 패스워드)
    // 반환 데이터 형식: ResponseEntity<Void> (응답 데이터 없음, 상태 코드만 반환)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsummer(
            @PathVariable Long id,
            @RequestBody ConsummerRequestDto consummerRequestDto
    ){
        // consummerService에서 소비자 정보를 삭제하는 메서드를 호출
        // id와 함께 전달된 패스워드를 사용하여 해당 소비자를 삭제
        consummerService.deleteConsummer(id, consummerRequestDto.getConsummerPassword());

        // 삭제 완료 후, 상태 코드 204 No Content를 반환 (응답 본문 없음)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
