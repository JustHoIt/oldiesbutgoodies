package com.hm.oldiesbutgoodies.common.config;

import com.hm.oldiesbutgoodies.auth.JwtAuthenticationFilter;
import com.hm.oldiesbutgoodies.auth.JwtProvider;
import com.hm.oldiesbutgoodies.auth.OAuth2AuthenticationSuccessHandler;
import com.hm.oldiesbutgoodies.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(JwtProvider jwtProvider, OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.jwtProvider = jwtProvider;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/v1/login",
                                "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }

}
