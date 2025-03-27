package com.hm.oldiesbutgoodies.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
    private String role;
    private String phoneNumber;
    private String address;
    private String profileImg;
    private String birthDate;
    private String gender;
}
