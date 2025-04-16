package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.domain.user.UserProfile;
import com.hm.oldiesbutgoodies.repository.UserProfileRepository;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("kakao".equals(registrationId)) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");

            String email = (String) account.get("email");
            String nickname = (String) profile.get("nickname");
            String profileUrl = (String) profile.get("profile_url");

            log.info("이메일 : {}, 이름: {}", email, nickname);

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newuser = User.from(email, nickname);
                        UserProfile newUserProfile = UserProfile.from(profile);
                        newuser.setUserProfile(newUserProfile);

                        return userRepository.save(newuser);
                    });

            Collection<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );

            return new DefaultOAuth2User(authorities, attributes, "id");

        }


        return oAuth2User;
    }
}
