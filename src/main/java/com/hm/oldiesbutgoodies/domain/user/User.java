package com.hm.oldiesbutgoodies.domain.user;

import com.hm.oldiesbutgoodies.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialLoginService socialLoginService;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public static User from(SignUpDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(dto.getPassword())
                .name(dto.getName())
                .role("ROLE_USER")
                .status(UserStatus.ACTIVE)
                .build();
    }
}
