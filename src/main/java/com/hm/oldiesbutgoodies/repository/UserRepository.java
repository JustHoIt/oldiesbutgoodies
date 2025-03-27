package com.hm.oldiesbutgoodies.repository;

import com.hm.oldiesbutgoodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);
}
