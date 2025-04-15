package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.user.Oauth2ServiceName;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.domain.user.UserProfile;
import com.hm.oldiesbutgoodies.domain.user.UserStatus;
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
    private Oauth2ServiceName oauth2ServiceName;
    private UserStatus userStatus;
    private String gender;

    public static UserDto from(User user, UserProfile userProfile) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(userProfile.getNickname())
                .role(user.getRole())
                .profileImg(userProfile.getProfileImg())
                .phoneNumber(user.getPhoneNumber())
                .address(userProfile.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .birthDate(userProfile.getBirthDate())
                .oauth2ServiceName(user.getOauth2ServiceName())
                .userStatus(user.getStatus())
                .gender(userProfile.getGender())
                .build();
    }
}
