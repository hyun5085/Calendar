package com.example.calendar.repository;

import com.example.calendar.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// 공통 인터페이스 정의
// 기본적인 CRUD 메서드들이 JpaRepositorydp 에 정의
// 필요한 공통 메서드를 여기에 추가
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // ID로 일정을 조회, 없으면 예외를 던지는 메서드
    // 요청 데이터 형식: id (Long)
    // 반환 데이터 형식: Schedule (존재하는 일정 객체 반환)
    // 예외: 없으면 ResponseStatusException (HTTP 404 NOT_FOUND)
    default Schedule findScheduleByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

}
