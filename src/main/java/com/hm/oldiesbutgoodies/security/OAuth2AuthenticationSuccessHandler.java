package com.hm.oldiesbutgoodies.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hm.oldiesbutgoodies.user.dto.response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper jacksonObjectMapper;

    public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider, ObjectMapper jacksonObjectMapper) {
        this.jwtProvider = jwtProvider;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // OAuth2User 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAttribute("role");
        String service = oAuth2User.getAttribute("service");

        // JWT 액세스 토큰 생성
        JwtResponse token = jwtProvider.generateToken(email, role);

        ObjectMapper mapper = new ObjectMapper();
        String json = jacksonObjectMapper.writeValueAsString(token);

        log.info("service: {}, email : {}, role : {}, AccessToken : {}", service, email, role, token.getAccessToken());

        // 클라이언트로 토큰 전송 - 예: JSON 응답으로 바로 출력

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
