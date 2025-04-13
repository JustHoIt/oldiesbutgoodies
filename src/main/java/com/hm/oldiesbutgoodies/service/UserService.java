package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.component.MailComponent;
import com.hm.oldiesbutgoodies.component.RedisComponent;
import com.hm.oldiesbutgoodies.dto.request.*;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.MailAuth;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisComponent redisComponent;
    private final PasswordEncoder passwordEncoder;
    private final MailComponent mailComponent;


    // 회원가입
    public ResponseDto signUp(SignUpDto dto) {

        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (this.userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new CustomException(ErrorCode.PHONENUMBER_ALREADY_EXISTS);
        }

        if (this.userRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        String uuid = UUID.randomUUID().toString();
        boolean passwordValid = passwordValidation(dto.getPassword());

        if (!passwordValid) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }

        String encPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        dto.setPassword(encPassword);

        User user = User.from(dto);
        userRepository.save(user);

        ResponseDto result = new ResponseDto();
        result.setMessage("회원가입에 완료했습니다.");


        log.info("{}님이 회원가입에 완료했습니다.", dto.getName());
        return result;
    }

    public ResponseDto mailAuth(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
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
    public ResponseDto userInfoUpdate(String tokenInfo, UserInfoUpdateDto dto) {

        if (tokenInfo == null || tokenInfo.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        if (!tokenInfo.equals(dto.getEmail())) {
            throw new CustomException(ErrorCode.MISMATCH_USER);
        }

        Optional<User> optionalUser = userRepository.findByEmail(tokenInfo);

        if (optionalUser.isEmpty()) {
            log.info(optionalUser.get().getName());
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        boolean passwordValid = passwordValidation(dto.getPassword());

        if (!passwordValid) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }

        String encPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());

        User user = optionalUser.get();
        user.setNickname(dto.getNickname());
        user.setPassword(encPassword);
        user.setProfileImg(dto.getProfileImg());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.preUpdate();

        userRepository.save(user);

        return new ResponseDto.setMessage("회원정보 수정이 완료 됐습니다.");
    }


    // 이메일 인증 체크
    public ResponseDto emailCheck(String email, String code) {
        MailAuth mailAuth = (MailAuth) redisComponent.get(email);
        ResponseDto result = new ResponseDto();

        if (!mailAuth.getCode().equals(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        if (mailAuth == null) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        } else {
            log.info("이메일 인증 체크 완료 : {}, code : {}", email, mailAuth.getCode());
            result.setMessage("이메일 인증 체크 완료");
            redisComponent.delete(email);
            return result;
        }
    }

    // 로그인
    public User authenticate(LoginRequest form) {

        Optional<User> optionalUser = findUserByLoginId(form.getId());

        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        User user = optionalUser.get();

        if (!this.passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_PASSWORD);
        }

        if (user.getStatus().equals("ACTIVE")) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }

        return user;
    }

    public Optional<User> findUserByLoginId(String loginId) {
        Optional<User> optionalUser;

        if (loginId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            optionalUser = userRepository.findByEmail(loginId);
            log.info("{}님이 이메일로 로그인했습니다.", optionalUser.get().getName());
            return optionalUser;
        } else if (loginId.matches("^[0-9]{10,11}$")) {
            optionalUser = userRepository.findByPhoneNumber(loginId);
            log.info("{}님이 휴대폰 번호로 로그인했습니다.", optionalUser.get().getName());
            return optionalUser;
        } else {
            return Optional.empty();
        }
    }

    public UserDto getUserInfo(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        return user.map(UserDto::getUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public OtherUserDto getOtherUserInfo(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        return user.map(OtherUserDto::getUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public boolean passwordValidation(String pwd) {
        if (pwd.length() < 8 || pwd.length() > 20 || pwd == null) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        String specialChars = "!@#$%^&*";

        for (char ch : pwd.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (specialChars.indexOf(ch) >= 0) {
                hasSpecialChar = true;
            }
        }

        return hasLetter && hasDigit && hasSpecialChar;
    }


    @Transactional
    public ResponseDto passwordReset(String email, String phoneNumber) {
        //TODO: UserService 랑 분리 필요해보임

        if (email == null || email.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()) {
            throw new CustomException(ErrorCode.INPUT_VALUE_REQUIRED);
        }

        Optional<User> user1 = userRepository.findByEmail(email);
        Optional<User> user2 = userRepository.findByPhoneNumber(phoneNumber);

        if (user1.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        if (user2.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        if (!Objects.equals(user1.get().getId(), user2.get().getId())) {
            throw new CustomException(ErrorCode.MISMATCH_USER);
        }

        User user = user1.orElseThrow(() ->
                new CustomException(ErrorCode.MISMATCH_USER));

        String newPwd = passwordRandomCode();

        log.info("new Pwd: {}", newPwd);

        mailComponent.passwordRest(newPwd, email);

        String encPassword = BCrypt.hashpw(newPwd, BCrypt.gensalt());

        user.setPassword(encPassword);
        userRepository.save(user);

        return new ResponseDto.setMessage(email + "로 메일을 발송했습니다.");

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

}
