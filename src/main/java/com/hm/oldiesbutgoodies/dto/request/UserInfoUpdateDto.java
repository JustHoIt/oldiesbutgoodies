package com.hm.oldiesbutgoodies.dto.request;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoUpdateDto {
    //TODO: 이름 변경하기\
    private String nickname;
    private String password;
    private String profileImg;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private String gender;
}
