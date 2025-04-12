package com.hm.oldiesbutgoodies.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    @NotBlank(message = "이메일은 필수로 입력해야합니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "example123@naver.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20글자 이하입니다.")
    @Schema(description = "비밀번호", example = "password1234")
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{10,16}$", message = "비밀번호는 숫자, 영어(대, 소문자), 특수문자(!, @, #, $, %, ^, &, *)만 가능합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20글자 이하입니다.")
    @Schema(description = "비밀번호 체크", example = "password1234")
    private String passwordCheck;

    @NotBlank(message = "이름은 필수로 입력해야합니다.")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "닉네임은 필수로 입력해야합니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "보관함 이름에는 한글, 영문, 숫자만 가능합니다")
    @Size(min = 6, max = 12, message = "닉네임은 6자 이상, 12글자 이하입니다.")
    @Schema(description = "닉네임", example = "고독한사자12")
    private String nickname;

    @NotBlank(message = "휴대폰 번호는 필수로 입력해야합니다.")
    @Pattern(regexp = "^[0-9]{10,11}")
    @Schema(description = "휴대폰 번호", example = "01033334444")
    private String phoneNumber;

    @Schema(description = "주소", example = "@@시 ##구 &&동 @@아파트 or @@빌라 $동 $호")
    private String address;

    @Schema(description = "프로필 이미지", example = "")
    private String profileImg;

    @Schema(description = "생일", example = "1996-05-09")
    private String birthDate;

    @Schema(description = "성별", example = "MALE")
    private String gender;
}
