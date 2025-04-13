package com.hm.oldiesbutgoodies.controller;

import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.dto.request.*;
import com.hm.oldiesbutgoodies.dto.response.JwtResponse;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/signUp",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }

    @GetMapping(value = "/signUp/emailCheck",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> emailCheck(@RequestParam String email, String code) {
        return ResponseEntity.ok(userService.emailCheck(email, code));
    }

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest form) {
        User user = userService.authenticate(form);
        JwtResponse jwtResponse = jwtProvider.generateToken(user.getEmail(), user.getRole());
        log.info("{}님이 로그인에 성공했습니다. token : {}", user.getName(), jwtResponse.getToken());
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping(value = "/getUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getUsername(token);
        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    @GetMapping(value = "/getOtherUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OtherUserDto> getOtherUserInfo(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getOtherUserInfo(email));
    }

    @PutMapping(value = "/infoUpdate",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> userInfoUpdate(@RequestHeader("Authorization") String token,
                                                      @RequestBody UserInfoUpdateDto dto) {
        String tokenInfo = jwtProvider.getUsername(token);
        return ResponseEntity.ok(userService.userInfoUpdate(tokenInfo, dto));
    }

    @PostMapping(value = "/users/mailAuth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signupMailAuth(@RequestParam String email) throws MessagingException {
        log.info("{} 인증 요청", email);
        return ResponseEntity.ok(userService.mailAuth(email));
    }

    @PutMapping("/users/pwdReset")
    public ResponseEntity<ResponseDto> passwordReset(@RequestParam String email, String phoneNumber) throws Exception {
        log.info("입력받은 이메일 : {}, 휴대폰 번호 : {}", email, phoneNumber);
        return ResponseEntity.ok(userService.passwordReset(email, phoneNumber));
    }

}
