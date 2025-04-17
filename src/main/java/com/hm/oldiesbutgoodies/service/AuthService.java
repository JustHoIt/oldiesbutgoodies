package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.component.MailComponent;
import com.hm.oldiesbutgoodies.component.RedisComponent;
import com.hm.oldiesbutgoodies.domain.user.MailAuth;
import com.hm.oldiesbutgoodies.domain.user.Oauth2ServiceName;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.domain.user.UserProfile;
import com.hm.oldiesbutgoodies.domain.user.UserStatus;
import com.hm.oldiesbutgoodies.dto.request.LoginRequest;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import com.hm.oldiesbutgoodies.repository.UserProfileRepository;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import com.hm.oldiesbutgoodies.utils.CodeGeneratorUtil;
import com.hm.oldiesbutgoodies.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Duration MAIL_AUTH_EXPIRATION = Duration.ofMinutes(3);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisComponent redisComponent;
    private final MailComponent mailComponent;


    // 회원가입
    @Transactional
    public ResponseDto signUp(SignUpDto dto) {
        validateSignUp(dto);

        User user = User.from(dto);
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        UserProfile userProfile = UserProfile.from(dto);
        user.setUserProfile(userProfile);

        userRepository.save(user);

        log.info("{}님이 회원가입에 완료했습니다.", dto.getName());
        return ResponseDto.setMessage("회원가입이 완료 됐습니다.");
    }


    private void validateSignUp(SignUpDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new CustomException(ErrorCode.PHONENUMBER_ALREADY_EXISTS);
        }
        if (userProfileRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        if (!PasswordUtil.passwordValid(dto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }


    public ResponseDto mailAuth(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String code = CodeGeneratorUtil.sixLetterRandomCode();
        log.info("Email Code: {}", code);

        mailComponent.signUpSend(code, email);

        MailAuth auth = MailAuth.builder()
                .code(code)
                .build();
        redisComponent.setExpiration(email, auth, MAIL_AUTH_EXPIRATION);

        return ResponseDto.setMessage(email + "로 메일을 발송했습니다.");
    }

    // 이메일 인증 체크
    public ResponseDto emailCheck(String email, String code) {
        MailAuth mailAuth = (MailAuth) redisComponent.get(email);

        if (mailAuth == null || !mailAuth.getCode().equals(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisComponent.delete(email);
        return ResponseDto.setMessage("이메일 인증이 완료 됐습니다.");
    }

    // 로그인
    public User authenticate(LoginRequest form) {

        Optional<User> optionalUser = findUserByLoginId(form.getId());

        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        if(user.getOauth2ServiceName() != Oauth2ServiceName.NONE){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!this.passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }

        return user;
    }


    public Optional<User> findUserByLoginId(String loginId) {
        Optional<User> optionalUser;

        if (loginId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            optionalUser = userRepository.findByEmail(loginId);
            return optionalUser;
        } else if (loginId.matches("^[0-9]{10,11}$")) {
            optionalUser = userRepository.findByPhoneNumber(loginId);
            return optionalUser;
        } else {
            return Optional.empty();
        }
    }

}
