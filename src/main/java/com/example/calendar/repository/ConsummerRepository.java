package com.example.calendar.repository;

import com.example.calendar.entity.Consummer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// 공통 인터페이스 정의
// 기본적인 CRUD 메서드들이 JpaRepositorydp 에 정의
// 필요한 공통 메서드를 여기에 추가
public interface ConsummerRepository extends JpaRepository<Consummer, Long> {

    // 소비자 이름으로 검색하는 메서드
    // 요청 데이터 형식: consummerName (String)
    // 반환 데이터 형식: Optional<Consummer> (해당 이름을 가진 소비자가 있을 경우 해당 객체, 없으면 Optional.empty())
    Optional<Consummer> findConsummerByConsummerName(String consummerName);

    // 소비자 이메일로 검색하는 메서드
    // 요청 데이터 형식: consummerEmail (String)
    // 반환 데이터 형식: Optional<Consummer> (해당 이메일을 가진 소비자가 있을 경우 해당 객체, 없으면 Optional.empty())
    Optional<Consummer> findByConsummerEmail(String email);

    // 이메일 중복 여부 체크
    // 요청 데이터 형식: consummerEmail (String)
    // 반환 데이터 형식: boolean (해당 이메일이 이미 존재하면 true, 아니면 false)
    boolean existsByConsummerEmail(String consummerEmail);

    // 소비자 이름으로 조회, 없으면 예외를 던지는 메서드
    // 요청 데이터 형식: consummerName (String)
    // 반환 데이터 형식: Consummer (존재하는 소비자 객체 반환)
    // 예외: 없으면 ResponseStatusException (HTTP 404 NOT_FOUND)
    default Consummer findConsummerByConsummerNameOrelseThrow(String consummerName){
        return findConsummerByConsummerName(consummerName).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Does not exist username = " + consummerName));
    }

    // ID로 소비자 조회, 없으면 예외를 던지는 메서드
    // 요청 데이터 형식: id (Long)
    // 반환 데이터 형식: Consummer (존재하는 소비자 객체 반환)
    // 예외: 없으면 ResponseStatusException (HTTP 404 NOT_FOUND)
    default Consummer findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

}

