package com.hm.oldiesbutgoodies.component;

import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.dto.response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // OAuth2User 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAttribute("role");

        // JWT 액세스 토큰 생성
        JwtResponse token = jwtProvider.generateToken(email, role);
        String jwtToken = token.getToken();

        log.info("이메일 : {}, role : {}, 토큰 : {}", email, role, jwtToken);

        // 클라이언트로 토큰 전송 - 예: JSON 응답으로 바로 출력

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{ \"accessToken\": \"" + jwtToken + "\" }");
        response.getWriter().flush();
    }
}
