package com.hm.oldiesbutgoodies.controller;


import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("/v1")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final MailService mailService;

    @GetMapping(value = "/signUp/mailAuth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> signUpMailAuth(@RequestParam String email) throws MessagingException {
        log.info("{} 인증 요청", email);
        return ResponseEntity.ok(mailService.signUp(email));
    }
}
