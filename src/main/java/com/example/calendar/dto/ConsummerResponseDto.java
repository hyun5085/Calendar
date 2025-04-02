package com.example.calendar.dto;

import com.example.calendar.entity.Consummer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
public class ConsummerResponseDto {

    private Long consummerId;           // 유저 고유 값(id)

    private String consummerName;       // 유저 이름

    private String consummerEmail;      // 유저 이메일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime consummerCreatedAt;    // 유저 생성일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime consummerUpdatedAt;   // 유저 수정일

    public ConsummerResponseDto(Long consummerId, String consummerName, String consummerEmail) {
        this.consummerId = consummerId;
        this.consummerName = consummerName;
        this.consummerEmail = consummerEmail;
    }

    public ConsummerResponseDto(Consummer consummer){
        this.consummerId = consummer.getConsummerid();
        this.consummerName = consummer.getConsummerName();
        this.consummerEmail = consummer.getConsummerEmail();
        this.consummerCreatedAt = consummer.getCreatedAt();
        this.consummerUpdatedAt = consummer.getUpdatedAt();

    }
}
