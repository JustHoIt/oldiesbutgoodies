package com.hm.oldiesbutgoodies.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {
    private String accessToken;
}
