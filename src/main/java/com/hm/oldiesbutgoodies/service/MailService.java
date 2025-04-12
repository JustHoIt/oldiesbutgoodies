package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.component.MailComponent;
import com.hm.oldiesbutgoodies.component.RedisComponent;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.MailAuth;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final UserRepository userRepository;
    private final MailComponent mailComponent;
    private final RedisComponent redisComponent;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    public ResponseDto signUp(String email) throws MessagingException {
        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 가입된 이메일이 존재합니다.");
        }

        String code = sixLetterRandomCode();
        log.info("Email Code: {}", code);

        mailComponent.signUpSend(code, email);

        Duration duration = Duration.ofMinutes(3);
        MailAuth auth = MailAuth.builder()
                .code(code)
                .build();
        redisComponent.setExpiration(email, auth, duration);

        return new ResponseDto.setMessage(email + "로 메일을 발송했습니다.");
    }


    @Transactional
    public ResponseDto passwordReset(String email, String phoneNumber) throws Exception {
        //TODO: UserService 랑 분리 필요해보임

        if (email == null || email.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()) {
            //TODO: 예외처리 조금 더 고민 해보기
            return new ResponseDto.setMessage("값을 비워둘 수 없습니다.");
        }

        Optional<User> user1 = userRepository.findByEmail(email);
        Optional<User> user2 = userRepository.findByPhoneNumber(phoneNumber);

        if (user1.isEmpty()) {
            return new ResponseDto.setMessage("일치하는 회원정보가 없습니다.1");
        }

        if (user2.isEmpty()) {
            return new ResponseDto.setMessage("일치하는 회원정보가 없습니다.2");
        }

        if (!Objects.equals(user1.get().getId(), user2.get().getId())) {
            //TODO: 예외처리 만들기
            return new ResponseDto.setMessage("일치하는 회원정보가 없습니다.3");
        }

        User user = user1.orElseThrow(() ->
                new EntityNotFoundException("일치하는 회원정보가 없습니다."));

        String newPwd = passwordRandomCode();

        log.info("new Pwd: {}", newPwd);

        mailComponent.passwordRest(newPwd, email);

        String encPassword = BCrypt.hashpw(newPwd, BCrypt.gensalt());

        user.setPassword(encPassword);
        userRepository.save(user);

        return new ResponseDto.setMessage(email + "로 메일을 발송했습니다.");

    }

    public String sixLetterRandomCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int n = random.nextInt(36);
            if (n > 25) {
                list.add(String.valueOf(n - 25));
            } else {
                list.add(String.valueOf((char) (n + 65)));
            }
        }

        for (String item : list) {
            code.append(item);
        }

        return code.toString();
    }

    public String passwordRandomCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(36);
            if (n > 25) {
                list.add(String.valueOf(n - 25));
            } else {
                list.add(String.valueOf((char) (n + 65)));
            }

            if (i == 9) {
                String[] symbol = {"!", "@", "#", "$", "%", "^", "&", "*"};
                int j = random.nextInt(symbol.length);
                list.add(symbol[j]);
            }
        }
        for (String item : list) {
            code.append(item);
        }

        return code.toString();
    }
}
