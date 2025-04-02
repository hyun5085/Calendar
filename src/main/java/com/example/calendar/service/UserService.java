package com.example.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public LoginResponseDto login(String userName, String password) {
        // 입력받은 userName, password와 일치하는 Database 조회
        Long index = userRepository.findIdByUserNameAndPassword(userName, password);

        return new LoginResponseDto(index);
    }

    public UserResponseDto findById(Long id) {

        return userRepository.findById(id);
    }

}
