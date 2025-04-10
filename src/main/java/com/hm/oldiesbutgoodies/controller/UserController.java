package com.hm.oldiesbutgoodies.controller;

import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.dto.request.LoginRequest;
import com.hm.oldiesbutgoodies.dto.request.OtherUserDto;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.request.UserDto;
import com.hm.oldiesbutgoodies.dto.response.JwtResponse;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.entity.User;
import com.hm.oldiesbutgoodies.service.UserService;
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

    @PostMapping(value = "/singUp",
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
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest form) throws Exception {
        User user = userService.authenticate(form);
        JwtResponse jwtResponse = jwtProvider.generateToken(user.getEmail(), user.getRole());
        log.info("{}님이 로그인에 성공했습니다. token : {}", user.getName(), jwtResponse.getToken());
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping(value = "/getUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization")String token) {
        String email = jwtProvider.getUsername(token);
        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    @GetMapping(value = "/getOtherUser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OtherUserDto> getOtherUserInfo(@RequestParam("email")String email) {
        return ResponseEntity.ok(userService.getOtherUserInfo(email));
    }


    // ✅TODO: 회원정보 수정


    // ✅TODO: 비밀번호 변경
}
