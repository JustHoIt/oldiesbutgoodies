package com.hm.oldiesbutgoodies.domain.user;

import com.hm.oldiesbutgoodies.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    @Column(nullable = false)
    private String name;

    private String role;

    @Enumerated(EnumType.STRING)
    private Oauth2ServiceName oauth2ServiceName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        userProfile.setUser(this);
    }

    public static User from(SignUpDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(dto.getPassword())
                .name(dto.getName())
                .role("ROLE_USER")
                .oauth2ServiceName(Oauth2ServiceName.NONE)
                .status(UserStatus.ACTIVE)
                .build();
    }

    public static User from(String email, String nickName) {
        return User.builder()
                .email(email)
                .name(nickName)
                .password(UUID.randomUUID().toString())
                .oauth2ServiceName(Oauth2ServiceName.KAKAO)
                .role("ROLE_USER")
                .status(UserStatus.ACTIVE)
                .build();
    }

    public static User from(Map<String, Object> attributes) {
        return User.builder()
                .email((String) attributes.get("email"))
                .phoneNumber((String) attributes.get(""))
                .password(UUID.randomUUID().toString())
                .name((String) attributes.get("name"))
                .role("ROLE_USER")
                .oauth2ServiceName(Oauth2ServiceName.NAVER)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
