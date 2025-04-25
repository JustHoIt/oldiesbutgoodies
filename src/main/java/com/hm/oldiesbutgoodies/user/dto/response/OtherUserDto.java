package com.hm.oldiesbutgoodies.user.dto.response;

import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.domain.UserProfile;
import lombok.*;

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


    public static OtherUserDto from(User user, UserProfile userProfile) {
        return OtherUserDto.builder()
                .nickname(userProfile.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
