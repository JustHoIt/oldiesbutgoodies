package com.hm.oldiesbutgoodies.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponseDto {
    private String message;

    public static ResponseDto setMessage(String message) {
        ResponseDto dto = new ResponseDto();
        dto.message = message;
        return dto;
    }

}

