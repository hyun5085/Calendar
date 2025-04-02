package com.example.calendar.service;

import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.dto.ScheduleRequestDto;
import com.example.calendar.dto.ScheduleResponseDto;
import com.example.calendar.entity.Consummer;
import com.example.calendar.entity.Schedule;
import com.example.calendar.repository.ConsummerRepository;
import com.example.calendar.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final ConsummerRepository consummerRepository;


    // 일정 생성
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto) {

        Consummer findConsummer = consummerRepository.findConsummerByConsummerNameOrelseThrow(scheduleRequestDto.getScheduleWriter());

        Schedule schedule = new Schedule(scheduleRequestDto);

        schedule.setConsummer(findConsummer);

        Schedule saveSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(saveSchedule);
    }

    // 일정 전체 조회
    public List<ScheduleResponseDto> findAllSchedules(){

        List<Schedule> findAllScheduls = scheduleRepository.findAll();

        List<ScheduleResponseDto> allScheduleList = new ArrayList<>();

        for (Schedule schedule : findAllScheduls) {
            allScheduleList.add(new ScheduleResponseDto(schedule));
        }
        return allScheduleList;
    }

    // 일정 단건 조회
    public ScheduleResponseDto findByScheduleId(Long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);

        if (optionalSchedule.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.NO_CONTENT, "Does not exists id: " + id);
        }

        Schedule findSchedule = optionalSchedule.get();

        return new ScheduleResponseDto((findSchedule));

    }


    // 일정 수정
    public ScheduleResponseDto updateSchedule(Long id, String scheduleWriter, String scheduleTitle, String scheduleContent, String userPassword) {

        // 기존 일정 조회
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        // 작성자 정보 가져오기
        Consummer consummer = schedule.getConsummer();

        // 작성자가 다르면 예외 발생 (403 Forbidden)
        if (!schedule.getConsummer().getConsummerName().equals(scheduleWriter)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this schedule.");
        }

        // 작성자의 비밀번호 검증
        if (consummer == null || consummer.getConsummerPassword() == null || !consummer.getConsummerPassword().equals(userPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password.");
        }

        // 일정 수정
        schedule.update(scheduleWriter, scheduleTitle, scheduleContent);

        // 수정된 일정 저장
        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);
    }


    public void deleteSchedule(Long id) {
        Schedule findSchedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        scheduleRepository.delete(findSchedule);

    }

//    // 유저 삭제
//    public void deleteConsummer(Long id, String consummerPassword) {
//        Consummer consummer = consummerRepository.findByIdOrElseThrow(id);
//        if (!consummer.getConsummerPassword().equals(consummerPassword)){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match.");
//        }
//
//        consummerRepository.delete(consummer);
//    }

}
