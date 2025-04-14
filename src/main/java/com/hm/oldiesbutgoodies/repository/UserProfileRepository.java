package com.hm.oldiesbutgoodies.repository;

import com.hm.oldiesbutgoodies.domain.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
   boolean existsByNickname(String nickname);

   Optional<UserProfile> findByUserId(Long id);
}
