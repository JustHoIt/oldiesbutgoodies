package com.hm.oldiesbutgoodies.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
    private final String code;
    private final int status;


    public CustomException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus().value();
    }

}
