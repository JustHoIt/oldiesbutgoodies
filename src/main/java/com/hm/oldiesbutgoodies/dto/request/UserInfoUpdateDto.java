package com.hm.oldiesbutgoodies.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoUpdateDto {
    //TODO: 이름 변경하기
    private String email;

    @NotBlank(message = "닉네임은 필수로 입력해야합니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "보관함 이름에는 한글, 영문, 숫자만 가능합니다")
    @Size(min = 6, max = 12, message = "닉네임은 6자 이상, 12글자 이하입니다.")
    @Schema(description = "닉네임", example = "고독한사자12")

    private String nickname;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20글자 이하입니다.")
    @Schema(description = "비밀번호", example = "password1234")
    private String password;

    @Schema(description = "프로필 이미지", example = "")
    private String profileImg;

    @NotBlank(message = "휴대폰 번호는 필수로 입력해야합니다.")
    @Pattern(regexp = "^[0-9]{10,11}")
    @Schema(description = "휴대폰 번호", example = "01033334444")
    private String phoneNumber;

    @Schema(description = "주소", example = "@@시 ##구 &&동 @@아파트 or @@빌라 $동 $호")
    private String address;

    @Schema(description = "생일", example = "1996-05-09")
    private LocalDate birthDate;

    @Schema(description = "성별", example = "MALE")
    private String gender;
}
