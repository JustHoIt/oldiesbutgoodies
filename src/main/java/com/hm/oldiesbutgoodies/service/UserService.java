package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseDto signUp(SignUpDto dto) {

        if(this.userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("중복된 이메일이 존재합니다.");
        }
        if(this.userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new RuntimeException("중복된 휴대폰 번호가 존재합니다.");
        }

        if(this.userRepository.existsByNickname(dto.getNickname())){
            throw new RuntimeException("중복된 닉네임이 존재합니다.");
        }

        String uuid = UUID.randomUUID().toString();
        String encPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        dto.setPassword(encPassword);

        User user = User.from(dto);
        userRepository.save(user);


    }
}
