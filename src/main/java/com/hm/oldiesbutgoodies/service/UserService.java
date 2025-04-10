package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.component.RedisComponent;
import com.hm.oldiesbutgoodies.dto.request.LoginRequest;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.MailAuth;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisComponent redisComponent;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
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

    // 이메일 인증 체크
    public ResponseDto emailCheck(String email, String code) {
        MailAuth mailAuth = (MailAuth) redisComponent.get(email);
        ResponseDto result = new ResponseDto();

        if (!mailAuth.getCode().equals(code)) {
            log.info("mailAuthCode: {}, code : {} ", mailAuth.getCode(), code);
            result.setMessage("이메일 인증코드가 일치하지 않습니다.");
            return result;
        }

        if (mailAuth == null) {
            log.info("이메일 인증 코드가 존재하지 않음");
            result.setMessage("이메일 인증코드가 존재하지 않거나, 유효기간이 만료되었습니다.");
            return result;
        } else {
            log.info("이메일 인증 체크 완료 : {}, code : {}", email, mailAuth.getCode());
            result.setMessage("이메일 인증 체크 완료");
            redisComponent.delete(email);
            return result;
        }
    }

    // 로그인
    public User authenticate(LoginRequest form) throws Exception {

        Optional<User> optionalUser = findUserByLoginId(form.getId());

        if (optionalUser.isEmpty()) {
            throw new Exception("NOT_MATCH_USER");
        }

        User user = optionalUser.get();

        if (!this.passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        if (user.getStatus().equals("ACTIVE")) {
            throw new Exception("회원이 이용 가능한 상태가 아닙니다.");
        }

        return user;
    }

    private Optional<User> findUserByLoginId(String loginId) {
        Optional<User> optionalUser;

        if (loginId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            optionalUser = userRepository.findByEmail(loginId);
            /*일치하지 않을때 오류 넣기*/
            log.info("{}님이 이메일로 로그인했습니다.", optionalUser.get().getName());
            return optionalUser;
        } else if (loginId.matches("^[0-9]{10,11}$")) {
            optionalUser = userRepository.findByPhoneNumber(loginId);
            /*일치하지 않을때 오류 넣기*/
            log.info("{}님이 휴대폰 번호로 로그인했습니다.", optionalUser.get().getName());
            return optionalUser;
        } else {
            return Optional.empty();
        }
    }
}
