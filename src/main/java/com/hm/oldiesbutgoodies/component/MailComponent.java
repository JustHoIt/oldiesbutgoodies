package com.hm.oldiesbutgoodies.component;

import com.hm.oldiesbutgoodies.dto.request.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailComponent {

    private final JavaMailSender mailSender;

    public void sendMail(MailDto mailDto) throws MessagingException {

        MimeMessagePreparator msg = new MimeMessagePreparator() {


            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper
                        = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mailDto.getTo());
                mimeMessageHelper.setFrom(mailDto.getFrom());
                mimeMessageHelper.setSubject(mailDto.getSubject());
                mimeMessageHelper.setText(mailDto.getText());
            }
        };

        sendResult(msg);
    }

    public void sendResult(MimeMessagePreparator msg) throws MailException {
        try {
            mailSender.send(msg);
            log.info("메일 전송에 성공했습니다.");
        } catch (MailException e) {
            log.error("메일 전송에 실해했습니다. {} ", e.getMessage());
        }
    }

    public void signUpSend(String code, String email) throws MessagingException {
        MailDto mailDto = MailDto.builder()
                .to(email)
                .from("Oldies But Goodies")
                .subject("OldiesButGoodies - 이메일 인증 ")
                .text("<H1>Oldies But Goodies 이메일 인증 코드입니다.</H1>" +
                        "</br>" +
                        "인증번호 : " + code +
                        "</br>" +
                        "회원가입 창으로 돌아가 위 6자리 코드를 입력해주세요.")
                .build();

        log.info("이메일 인증 메일 발송완료 To : {}", email);

        sendMail(mailDto);
    }
}
