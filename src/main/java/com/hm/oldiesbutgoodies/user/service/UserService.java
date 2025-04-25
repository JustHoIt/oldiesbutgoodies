package com.hm.oldiesbutgoodies.user.service;

import com.hm.oldiesbutgoodies.common.component.MailComponent;
import com.hm.oldiesbutgoodies.user.dto.response.OtherUserDto;
import com.hm.oldiesbutgoodies.user.dto.response.UserDto;
import com.hm.oldiesbutgoodies.user.dto.request.UserInfoUpdateDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.domain.UserProfile;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.user.repository.UserProfileRepository;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
import com.hm.oldiesbutgoodies.common.utils.PasswordUtil;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MailComponent mailComponent;

    /*
     * 사용자 정보 업데이트
     * */
    @Transactional
    public ResponseDto userInfoUpdate(String tokenInfo, UserInfoUpdateDto dto) {

        validateUserToken(tokenInfo, dto);

        User user = findUserByEmail(tokenInfo);
        UserProfile profile = findUserProfile(user);

        updateUser(user, dto);
        updateUserProfile(profile, dto);

        userRepository.save(user);
        userProfileRepository.save(profile);

        log.info("회원 정보 수정 완료 - email: {}", dto.getEmail());

        return ResponseDto.setMessage("회원정보 수정이 완료 됐습니다.");
    }

    private void validateUserToken(String tokenInfo, UserInfoUpdateDto dto) {
        if (tokenInfo == null || tokenInfo.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!tokenInfo.equals(dto.getEmail())) {
            throw new CustomException(ErrorCode.USER_MISMATCH);
        }
    }

    public UserDto getUserInfo(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomException(ErrorCode.INPUT_VALUE_REQUIRED);
        }

        User user = findUserByEmail(email);

        UserProfile profile = findUserProfile(user);

        return UserDto.from(user, profile);
    }

    public OtherUserDto getOtherUserInfo(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomException(ErrorCode.INPUT_VALUE_REQUIRED);
        }

        User user = findUserByEmail(email);

        UserProfile profile = findUserProfile(user);

        return OtherUserDto.from(user, profile);
    }

    /*
     * 비밀번호 초기화
     * */
    @Transactional
    public ResponseDto passwordReset(String email, String phoneNumber) {
        validateResetInput(email, phoneNumber);
        User user = verifyMatchingUser(email, phoneNumber);
        String tempPassword = PasswordUtil.passwordRandomCode();

        sendResetPasswordMail(tempPassword, email);
        updateUserPassword(user, tempPassword);

        log.info("비밀번호 초기화 요청됨 - email: {}", email);

        return ResponseDto.setMessage(email + "로 메일을 발송했습니다.");
    }

    private void validateResetInput(String email, String phoneNumber) {
        if (email == null || email.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()) {
            throw new CustomException(ErrorCode.INPUT_VALUE_REQUIRED);
        }
    }

    private User verifyMatchingUser(String email, String phoneNumber) {
        User user1 = findUserByEmail(email);
        User user2 = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user1.getId().equals(user2.getId())) {
            throw new CustomException(ErrorCode.USER_MISMATCH);
        }
        return user1;
    }

    private void sendResetPasswordMail(String password, String email) {
        mailComponent.passwordRest(password, email);
    }

    private void updateUserPassword(User user, String password) {
        user.setPassword(PasswordUtil.hashPassword(password));
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private UserProfile findUserProfile(User user) {
        return userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_PROFILE_NOT_FOUND));
    }

    private void updateUser(User user, UserInfoUpdateDto dto) {
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getPassword() == null || !PasswordUtil.passwordValid(dto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }

        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
    }

    private void updateUserProfile(UserProfile profile, UserInfoUpdateDto dto) {
        profile.setProfileImg(dto.getProfileImg());
        profile.setNickname(dto.getNickname());
        profile.setAddress(dto.getAddress());
        profile.setBirthDate(dto.getBirthDate());
        profile.setGender(dto.getGender());
    }

}
