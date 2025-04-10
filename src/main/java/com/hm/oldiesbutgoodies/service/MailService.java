package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.component.MailComponent;
import com.hm.oldiesbutgoodies.component.RedisComponent;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.MailAuth;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final UserRepository userRepository;
    private final MailComponent mailComponent;
    private final RedisComponent redisComponent;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseDto signUp(String email) throws MessagingException {
        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 가입된 이메일이 존재합니다.");
        }

        String code = randomCode();
        log.info("Email Code: {}", code);

        mailComponent.signUpSend(code, email);

        Duration duration = Duration.ofMinutes(3);
        MailAuth auth = MailAuth.builder()
                .code(code)
                .build();
        redisComponent.setExpiration(email, auth, duration);

        return new ResponseDto.setMessage(email + "메시지 발송했습니다.");
    }

    public String randomCode() {
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
}
