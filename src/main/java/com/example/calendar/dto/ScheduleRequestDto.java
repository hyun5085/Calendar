package com.example.calendar.dto;

import lombok.Getter;

@Getter //  클래스의 모든 필드에 대한 Getter 메서드를 자동으로 생성해 줌
public class ScheduleRequestDto {           // 요청 Dto

    private String scheduleWriter;            // 일정 작성자
    private String scheduleTitle;             // 일정 제목
    private String scheduleContent;           // 일정 내용

}
