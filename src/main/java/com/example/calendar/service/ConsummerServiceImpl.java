package com.example.calendar.service;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.entity.Consummer;
import com.example.calendar.exception.CustomException;
import com.example.calendar.exception.ErrorCode;
import com.example.calendar.repository.ConsummerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 의존성 주입을 위한 어노테이션
public class ConsummerServiceImpl implements ConsummerService{

    private final ConsummerRepository consummerRepository;

    // 유저 생성
    // 유저 생성 요청을 처리하고, 이메일 중복 체크 후, 새 유저를 생성하여 응답 반환
    // 요청 데이터 형식: ConsummerRequestDto (사용자 이름, 이메일 등)
    // 반환 데이터 형식: ConsummerResponseDto (저장된 소비자 정보)
    public ConsummerResponseDto saveConsummer(ConsummerRequestDto consummerRequestDto) {

        // 이메일 중복 체크
        if (consummerRepository.existsByConsummerEmail(consummerRequestDto.getConsummerEmail())) {
            // 이메일이 이미 존재하면 예외 처리
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 이메일 중복이 없으면 유저 생성
        // 요청 데이터로부터 Consummer 엔티티 객체 생성
        Consummer consummer = new Consummer(consummerRequestDto);

        // 유저 데이터베이스에 저장
        Consummer savedConsummer = consummerRepository.save(consummer);
        // 저장된 소비자의 정보를 반환 DTO 형태로 변환하여 반환
        return new ConsummerResponseDto(savedConsummer);
    }

    // 단건 조회
    // 특정 소비자의 ID를 받아 해당 소비자의 정보를 조회하고 응답 반환
    // 요청 데이터 형식: Long id (소비자 ID)
    // 반환 데이터 형식: ConsummerResponseDto (조회된 소비자 정보)
    public ConsummerResponseDto findByConsummerId(Long id) {

        // ID로 소비자 조회 (Optional을 사용하여 존재 여부를 처리)
        Optional<Consummer> optionalConsummer = consummerRepository.findById(id);

        // 소비자가 존재하지 않으면 예외 처리
        if (optionalConsummer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Does not exists id: " + id);
        }

        // 존재하면 해당 소비자를 반환 DTO 형태로 변환하여 반환
        Consummer findConsummer = optionalConsummer.get();

        // 존재하는 소비자 정보를 DTO 형태로 변환하여 반환
        // Consummer 엔티티 객체인 findConsummer를 ConsummerResponseDto 객체로 변환하여 반환
        // 반환된 DTO는 클라이언트에게 전달될 소비자 정보를 담고 있음
        return new ConsummerResponseDto(findConsummer);
    }

    // 전체 조회
    // 모든 소비자 정보를 조회하여 리스트 형태로 반환
    // 요청 데이터 형식: 없음 (전체 소비자 조회)
    // 반환 데이터 형식: List<ConsummerResponseDto> (조회된 모든 소비자 정보 리스트)
    public List<ConsummerResponseDto> findAllConsummer() {

        // 모든 소비자 정보를 조회
        List<Consummer> findAllConsummer = consummerRepository.findAll();

        // 조회된 소비자 정보를 DTO 리스트로 변환하여 반환
        List<ConsummerResponseDto> allConsummerList = new ArrayList<>();

        for (Consummer consummer : findAllConsummer) {
            allConsummerList.add(new ConsummerResponseDto(consummer));
        }

        // 전체 소비자 목록 반환
        return allConsummerList;
    }

    // 패스워드 수정
    // 주어진 ID에 해당하는 소비자의 패스워드를 수정
    // 요청 데이터 형식: Long (소비자 ID), String (현재 비밀번호, 새 비밀번호)
    // 반환 데이터 형식: 없음 (수정 완료)
    // @Transactional : 트랜잭션을 적용하여 데이터 변경 시에만 DB에 반영되도록 보장
    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {

        // 소비자 정보를 ID로 조회 (예외 처리)
        Consummer findConsummer = consummerRepository.findByIdOrElseThrow(id);

        // 기존 비밀번호가 일치하는지 확인
        if(!findConsummer.getConsummerPassword().equals(oldPassword)){
            // 비밀번호가 다르면 예외 처리
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " Password fail ");
        }

        // 비밀번호가 일치하면 새로운 비밀번호로 업데이트
        findConsummer.updateConsummerPassword(newPassword);

    }

    // 유저 삭제
    // 주어진 ID에 해당하는 소비자를 삭제
    // 요청 데이터 형식: Long (소비자 ID), String (비밀번호)
    // 반환 데이터 형식: 없음 (삭제 완료)
    public void deleteConsummer(Long id, String consummerPassword) {

        // 소비자 정보를 ID로 조회 (예외 처리)
        Consummer consummer = consummerRepository.findByIdOrElseThrow(id);

        // 비밀번호가 일치하는지 확인
        if (!consummer.getConsummerPassword().equals(consummerPassword)){

            // 비밀번호가 다르면 예외 처리
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match.");
        }

        // 비밀번호가 맞으면 소비자 삭제
        consummerRepository.delete(consummer);
    }


    // 로그인 기능 추가
    // 주어진 이메일과 비밀번호로 소비자 로그인 처리
    // 요청 데이터 형식: String (이메일, 비밀번호)
    // 반환 데이터 형식: Consummer (로그인된 소비자 객체)
    public Consummer login(String email, String password) {

        // 이메일로 소비자 조회 (이메일이 없으면 예외 처리)
        Consummer consummer = consummerRepository.findByConsummerEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호가 일치하는지 확인
        if (!consummer.getConsummerPassword().equals(password)) {

            // 비밀번호가 일치하지 않으면 예외 처리
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인 성공 시 소비자 객체 반환
        return consummer;
    }

    @Override
    public boolean validateConsummerPassword(Long id, String password) {
        Consummer consummer = consummerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return consummer.getConsummerPassword().equals(password);
    }

}
