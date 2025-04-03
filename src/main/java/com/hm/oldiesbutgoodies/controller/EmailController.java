package com.hm.oldiesbutgoodies.controller;


import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("/v1")
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @PostMapping(value = "/signUp/mailAuth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signUpMailAuth(@RequestBody String email) throws MessagingException {
        return ResponseEntity.ok(mailService.signUp(email));
    }
}
