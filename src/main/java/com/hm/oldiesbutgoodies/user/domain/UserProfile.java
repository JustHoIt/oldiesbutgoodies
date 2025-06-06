package com.hm.oldiesbutgoodies.user.domain;

import com.hm.oldiesbutgoodies.common.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.user.dto.request.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false, unique = true)
    private String nickname;

    private String address;
    private String profileImg;
    private LocalDate birthDate;
    private String gender;

    public static UserProfile from(SignUpDto dto) {
        return UserProfile.builder()
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .profileImg(dto.getProfileImg())
                .birthDate(LocalDate.parse(dto.getBirthDate()))
                .gender(dto.getGender())
                .build();
    }

    public static UserProfile from(Map<String, Object> attributes) {
        String birthYear = (String) attributes.getOrDefault("birthyear", null);
        String birthday = (String) attributes.getOrDefault("birthday", null);
        LocalDate parseBirth = null;

        if (birthYear != null && birthday != null) {
            parseBirth = LocalDate.parse(birthYear + "-" + birthday);
        }

        return UserProfile.builder()
                .nickname((String) attributes.getOrDefault("nickname", null))
                .address((String) attributes.getOrDefault("address", null))
                .profileImg((String) attributes.getOrDefault("profile_image", null))
                .birthDate(parseBirth)
                .gender((String) attributes.getOrDefault("gender", null))
                .build();
    }

}
