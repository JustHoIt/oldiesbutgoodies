package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherUserDto {
    private String email;
    private String nickname;
    private String profileImg;
    private String phoneNumber;
    private LocalDateTime createdAt;


    public static OtherUserDto getUser(User user) {
        return OtherUserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
