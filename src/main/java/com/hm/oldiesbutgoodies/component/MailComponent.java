package com.hm.oldiesbutgoodies.component;

import com.hm.oldiesbutgoodies.dto.request.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailComponent {

    @Value("${spring.mail.username}")
    private String from;
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
                mimeMessageHelper.setText(mailDto.getText(), true);
            }
        };

        sendResult(msg, mailDto.getTo());
    }

    public void sendResult(MimeMessagePreparator msg, String email) throws MailException {
        try {
            mailSender.send(msg);
            log.info("메일 전송에 성공했습니다. To : {}", email);
        } catch (MailException e) {
            log.error("메일 전송에 실패했습니다. {} ", e.getMessage());
        }
    }

    public void signUpSend(String code, String email) throws MessagingException {
        MailDto mailDto = MailDto.builder()
                .to(email)
                .from(from)
                .subject("OldiesButGoodies - 이메일 인증 ")
                .text("<H1>Oldies But Goodies 이메일 인증 코드입니다.</H1>" +
                        "</br>" +
                        "인증번호 :  [<strong> " + code + "</strong> ]" +
                        "</br>" +
                        " 회원가입 창으로 돌아가 위 6자리 코드를 입력해주세요.")
                .build();


        sendMail(mailDto);
    }

    public void passwordRest(String newPwd, String email) throws MessagingException {
        MailDto mailDto = MailDto.builder()
                .to(email)
                .from(from)
                .subject("OldiesButGoodies - 임시 비밀번호 ")
                .text("<H1>Oldies But Goodies 임시 비밀번호 입니다.</H1>" +
                        "</br>" +
                        "임시 비밀번호 :  [<strong> " + newPwd + "</strong> ]" +
                        "</br>" +
                        " 로그인 후 반드시 비밀번호를 재설정 해주세요.")
                .build();

        sendMail((mailDto));
    }
}
