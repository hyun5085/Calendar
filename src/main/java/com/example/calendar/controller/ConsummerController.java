package com.example.calendar.controller;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.dto.UpdatePasswordRequestDto;
import com.example.calendar.service.ConsummerService;
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
    @PostMapping("/signup")
    public ResponseEntity<ConsummerResponseDto> saveConsummer(@RequestBody ConsummerRequestDto consummerRequestDto){
    //공유 / Http + 어떠한 방식으로 반환을 할건지?     / 변수명 : saveConsummer  /형식은 ConsummerRequestDto 형태로 반환



        return new ResponseEntity<>(consummerService.saveConsummer(consummerRequestDto),HttpStatus.CREATED);
    }

    // 유저 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ConsummerResponseDto> findByConsummerId(@PathVariable Long id){
        ConsummerResponseDto consummerResponseDto = consummerService.findByConsummerId(id);

        return new ResponseEntity<>(consummerResponseDto, HttpStatus.OK);
    }

    // 유저 전체 조회
    @GetMapping
    public ResponseEntity<List<ConsummerResponseDto>> findAllConsummer(){

        List<ConsummerResponseDto> allConsummer = consummerService.findAllConsummer();

        return new ResponseEntity<>(allConsummer, HttpStatus.OK);
    }

    // 유저 패스워드 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> consummerUpdatePassword(@PathVariable Long id,
                                                        @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto){
        consummerService.updatePassword(
                id,
                updatePasswordRequestDto.getOldPassword(),
                updatePasswordRequestDto.getNewPassword()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsummer(
            @PathVariable Long id,
            @RequestBody ConsummerRequestDto consummerRequestDto
    ){
        consummerService.deleteConsummer(id, consummerRequestDto.getConsummerPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
