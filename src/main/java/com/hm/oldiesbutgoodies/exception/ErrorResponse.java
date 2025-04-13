package com.hm.oldiesbutgoodies.exception;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final String code;
    private final String path;

    public ErrorResponse(ErrorCode errorCode, String path) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().getReasonPhrase();
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.path = path;
    }
}
