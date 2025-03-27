package com.hm.oldiesbutgoodies.controller;

import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/singUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }

}
