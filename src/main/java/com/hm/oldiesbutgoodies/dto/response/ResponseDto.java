package com.hm.oldiesbutgoodies.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDto {
    private String message;

    public static ResponseDto setMessage(String message) {
        ResponseDto dto = new ResponseDto();
        dto.message = message;
        return dto;
    }

}

