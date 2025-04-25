package com.hm.oldiesbutgoodies.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MailDto {
    private String to;
    private String from;
    private String subject;
    private String text;
}
