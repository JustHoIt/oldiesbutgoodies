package com.hm.oldiesbutgoodies.service;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    // 🚨 FIXME: 비밀번호 특수문자 없어도 가입되는 오류
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
            log.info("mailAuthCode: {}, code : {} ", mailAuth.getCode(), code);
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH);
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
            //TODO: 예외처리
            log.info("{}님이 이메일로 로그인했습니다.", optionalUser.get().getName());
            return optionalUser;
        } else if (loginId.matches("^[0-9]{10,11}$")) {
            optionalUser = userRepository.findByPhoneNumber(loginId);
            //TODO: 예외처리
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
                .orElseThrow();
    }

    public OtherUserDto getOtherUserInfo(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        return user.map(OtherUserDto::getUser)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));
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

}
