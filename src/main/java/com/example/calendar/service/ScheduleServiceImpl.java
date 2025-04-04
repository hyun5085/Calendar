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
    // 일정 생성 요청을 처리하고, 일정 작성자 정보 확인 후, 일정을 저장하고 응답 반환
    // 요청 데이터 형식: ScheduleRequestDto (일정 제목, 내용, 작성자 등)
    // 반환 데이터 형식: ScheduleResponseDto (저장된 일정 정보)
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto) {

        // 작성자 이름을 통해 소비자 조회, 없으면 예외 발생
        Consummer findConsummer = consummerRepository.findConsummerByConsummerNameOrelseThrow(scheduleRequestDto.getScheduleWriter());

        // 요청 데이터로부터 Schedule 엔티티 객체 생성
        Schedule schedule = new Schedule(scheduleRequestDto);

        // 일정의 작성자(consummer) 설정
        schedule.setConsummer(findConsummer);

        // 유저 데이터를 데이터베이스에 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // 저장된 일정 정보를 반환 DTO 형태로 변환하여 반환
        return new ScheduleResponseDto(saveSchedule);
    }

    // 일정 전체 조회
    // 모든 일정을 조회하고, 목록을 반환
    // 반환 데이터 형식: List<ScheduleResponseDto> (전체 일정 목록)
    public List<ScheduleResponseDto> findAllSchedules(){

        // 모든 일정 조회
        List<Schedule> findAllScheduls = scheduleRepository.findAll();

        // 일정 목록을 담을 리스트를 생성
        List<ScheduleResponseDto> allScheduleList = new ArrayList<>();

        // 조회된 일정들을 ScheduleResponseDto로 변환하여 리스트에 추가
        for (Schedule schedule : findAllScheduls) {
            allScheduleList.add(new ScheduleResponseDto(schedule));
        }

        // 전체 일정 목록 반환
        return allScheduleList;
    }

    // 일정 단건 조회
    // 특정 ID를 가진 일정을 조회하고, 해당 일정의 정보를 반환
    // 요청 데이터 형식: Long id (일정 ID)
    // 반환 데이터 형식: ScheduleResponseDto (조회된 일정 정보)
    public ScheduleResponseDto findByScheduleId(Long id) {

        // 주어진 id를 기준으로 일정 조회
        // findById는 Optional<Schedule> 객체를 반환, 일정이 존재하지 않으면 빈 Optional이 반환
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);

        // ID로 일정 조회, 없으면 예외 발생
        if (optionalSchedule.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.NO_CONTENT, "Does not exists id: " + id);
        }

        // 조회된 일정
        Schedule findSchedule = optionalSchedule.get();

        // 조회된 일정 정보를 ScheduleResponseDto로 변환하여 반환
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



}
