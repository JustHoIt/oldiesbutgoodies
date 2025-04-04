package com.hm.oldiesbutgoodies.controller;

import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/singUp",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }

}
