package com.example.calendar.service;

import com.example.calendar.dto.ConsummerRequestDto;
import com.example.calendar.dto.ConsummerResponseDto;
import com.example.calendar.dto.LoginRequestDto;
import com.example.calendar.dto.LoginResponseDto;
import com.example.calendar.entity.Consummer;
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
@RequiredArgsConstructor
public class ConsummerServiceImpl implements ConsummerService{

    private final ConsummerRepository consummerRepository;

    // 유저 생성
    public ConsummerResponseDto saveConsummer(ConsummerRequestDto consummerRequestDto) {

        Consummer consummer = new Consummer(consummerRequestDto);

        Consummer savedConsummer = consummerRepository.save(consummer);

        return new ConsummerResponseDto(savedConsummer);
    }

    // 단건 조회

    public ConsummerResponseDto findByConsummerId(Long id) {

        Optional<Consummer> optionalConsummer = consummerRepository.findById(id);

        if (optionalConsummer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Does not exists id: " + id);
        }

        Consummer findConsummer = optionalConsummer.get();

        return new ConsummerResponseDto(findConsummer);
    }

    // 전체 조회
    public List<ConsummerResponseDto> findAllConsummer() {
        List<Consummer> findAllConsummer = consummerRepository.findAll();

        List<ConsummerResponseDto> allConsummerList = new ArrayList<>();

        for (Consummer consummer : findAllConsummer) {
            allConsummerList.add(new ConsummerResponseDto(consummer));
        }

        return allConsummerList;
    }

    // 패스워드 수정
    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {

        Consummer findConsummer = consummerRepository.findByIdOrElseThrow(id);

        if(!findConsummer.getConsummerPassword().equals(oldPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " Password fail ");
        }

        findConsummer.updateConsummerPassword(newPassword);

    }

    // 유저 삭제
    public void deleteConsummer(Long id, String consummerPassword) {
        Consummer consummer = consummerRepository.findByIdOrElseThrow(id);
        if (!consummer.getConsummerPassword().equals(consummerPassword)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match.");
        }

        consummerRepository.delete(consummer);
    }


    // 로그인 기능 추가
    public Consummer login(String email, String password) {
        Consummer consummer = consummerRepository.findByConsummerEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!consummer.getConsummerPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return consummer;
    }
}
