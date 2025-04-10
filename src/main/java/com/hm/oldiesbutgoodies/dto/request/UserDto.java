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
public class UserDto {
    private String email;
    private String name;
    private String nickname;
    private String role;
    private String profileImg;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate birthDate;
    private String socialLoginType;
    private String gender;

    public static UserDto getUser(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .role(user.getRole())
                .profileImg(user.getProfileImg())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .birthDate(user.getBirthDate())
                .socialLoginType(user.getSocialLoginType())
                .gender(user.getGender())
                .build();
    }
}
