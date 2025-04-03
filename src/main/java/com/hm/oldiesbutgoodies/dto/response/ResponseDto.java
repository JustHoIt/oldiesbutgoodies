package com.hm.oldiesbutgoodies.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDto {
    private String message;

    public static class setMessage extends ResponseDto {
        public setMessage(String message) {
            super();
        }
    }
}
