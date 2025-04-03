package com.hm.oldiesbutgoodies.entity;

import com.hm.oldiesbutgoodies.dto.request.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String phoneNumber;

    private String address;
    private String profileImg;
    private String birthDate;
    private String gender;
    private String socialLoginType;
    private String status;

    // @Column(updatable = false) 처음 생성된 값(예: createdAt)이 변경되지 않도록 보호
    // JPA가 UPDATE 쿼리를 실행할 때 해당 컬럼을 제외, 데이터의 무결성 유지
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 자동으로 updatedAt 값을 갱신 → 매번 updatedAt = LocalDateTime.now();를 직접 호출할 필요 X
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static User from(SignUpDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .role("")
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .profileImg(dto.getProfileImg())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .socialLoginType("NONE")
                .status("")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
