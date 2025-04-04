package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseDto signUp(SignUpDto dto) {

        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("중복된 이메일이 존재합니다.");
        }
        if (this.userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new RuntimeException("중복된 휴대폰 번호가 존재합니다.");
        }

        if (this.userRepository.existsByNickname(dto.getNickname())) {
            throw new RuntimeException("중복된 닉네임이 존재합니다.");
        }

        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new RuntimeException("입력하신 비밀번호와 비밀번호 체크가 같지않습니다.");
        }

        String uuid = UUID.randomUUID().toString();
        String encPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        dto.setPassword(encPassword);

        User user = User.from(dto);
        userRepository.save(user);

        ResponseDto result = new ResponseDto();
        result.setMessage("회원가입에 완료했습니다.");


        log.info("{}님이 회원가입에 완료했습니다.", dto.getName());
        return result;
    }
}
