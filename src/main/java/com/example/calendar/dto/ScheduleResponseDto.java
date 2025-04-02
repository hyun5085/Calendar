package com.example.calendar.dto;

import com.example.calendar.entity.Consummer;
import com.example.calendar.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
public class ScheduleResponseDto {            // 응답 Dto

    private String scheduleWriter;            // 일정 작성자

    private String scheduleTitle;             // 일정 제목

    private String scheduleContent;           // 일정 내용

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduleCreatedAt;    // 일정 생성일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduleUpdatedAt;   // 일정 수정일


    public ScheduleResponseDto(Schedule schedule){

        this.scheduleWriter = schedule.getConsummer().getConsummerName();
        this.scheduleTitle = schedule.getScheduleTitle();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleCreatedAt = schedule.getCreatedAt();
        this.scheduleUpdatedAt = schedule.getUpdatedAt();

    }


}
