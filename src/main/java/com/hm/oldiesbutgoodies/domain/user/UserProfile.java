package com.hm.oldiesbutgoodies.domain.user;

import com.hm.oldiesbutgoodies.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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


}
