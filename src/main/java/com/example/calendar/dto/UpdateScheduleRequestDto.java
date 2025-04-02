package com.example.calendar.dto;

import lombok.Getter;

@Getter
public class UpdateScheduleRequestDto {
    private String scheduleWriter;  // 일정 작성자
    private String scheduleTitle;   // 일정 제목
    private String scheduleContent; // 일정 내용
    private String userPassword;    // 작성자의 비밀번호 (Consummer의 비밀번호)
}
