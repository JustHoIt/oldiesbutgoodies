package com.hm.oldiesbutgoodies.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "", "내부 서버 오류"),
    INPUT_VALUE_REQUIRED(HttpStatus.BAD_REQUEST, "", "값을 비워둘 수 없습니다."),
    USER_NOT_AUTHOR(HttpStatus.BAD_REQUEST, "", "권한이 없습니다."),
    USER_NOT_ACTIVE(HttpStatus.FORBIDDEN, "", "활동가능한 회원이 아닙니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "회원정보가 존재하지 않습니다."),
    USER_PROFILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "회원정보가 존재하지 않습니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "", "회원정보가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "", "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "", "중복된 이메일이 존재합니다."),
    EMAIL_VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "", "이메일 인증번호가 올바르지 않습니다."),
    PHONENUMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "", "중복된 휴대폰번호가 존재합니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "", "중복된 닉네임이 존재합니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "", "비밀번호 형식이 올바르지 않습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "", "잘못된 코드이거나, 유호기간이 만료되었습니다."),
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "게시글이 존재하지 않거나, 잘못된 접근입니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "댓글이 존재하지 않거나, 잘못된 접근입니다."),
    FILE_SAVE_FAILED_(HttpStatus.BAD_REQUEST, "", "파일 저장에 실패했습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "", "상품이 존재하지 않거나, 잘못된 접근입니다."),
    COMMENT_RESOURCE_MISMATCH(HttpStatus.BAD_REQUEST, "", "부모 댓글과 일치하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
