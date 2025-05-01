package com.hm.oldiesbutgoodies.user.controller;

import com.hm.oldiesbutgoodies.security.JwtProvider;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.dto.request.LoginRequest;
import com.hm.oldiesbutgoodies.user.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.user.dto.request.UserInfoUpdateDto;
import com.hm.oldiesbutgoodies.user.dto.response.JwtResponse;
import com.hm.oldiesbutgoodies.user.dto.response.OtherUserDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.user.dto.response.UserDto;
import com.hm.oldiesbutgoodies.user.service.AuthService;
import com.hm.oldiesbutgoodies.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/signUp",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    @GetMapping(value = "/signUp/emailCheck",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> emailCheck(@RequestParam String email, String code) {
        return ResponseEntity.ok(authService.emailCheck(email, code));
    }

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest form) {
        User user = authService.authenticate(form);
        JwtResponse jwtResponse = jwtProvider.generateToken(user.getEmail(), user.getRole());
        log.info("{}님이 로그인에 성공했습니다. token : {}", user.getName(), jwtResponse.getAccessToken());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/users/mailAuth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signupMailAuth(@RequestParam String email) {
        log.info("{} 인증 요청", email);
        return ResponseEntity.ok(authService.mailAuth(email));
    }

    @GetMapping(value = "/getUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String accessToken) {
        String email = jwtProvider.getUserEmail(accessToken);
        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    @GetMapping(value = "/getOtherUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OtherUserDto> getOtherUserInfo(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getOtherUserInfo(email));
    }

    @PutMapping(value = "/infoUpdate",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> userInfoUpdate(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody UserInfoUpdateDto dto) {
        String tokenInfo = jwtProvider.getUserEmail(accessToken);
        return ResponseEntity.ok(userService.userInfoUpdate(tokenInfo, dto));
    }

    @PutMapping("/users/pwdReset")
    public ResponseEntity<ResponseDto> passwordReset(@RequestParam String email, String phoneNumber) throws Exception {
        log.info("입력받은 이메일 : {}, 휴대폰 번호 : {}", email, phoneNumber);
        return ResponseEntity.ok(userService.passwordReset(email, phoneNumber));
    }

}
