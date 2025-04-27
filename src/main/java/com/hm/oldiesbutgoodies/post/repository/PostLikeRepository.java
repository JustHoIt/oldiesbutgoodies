package com.hm.oldiesbutgoodies.post.repository;

import com.hm.oldiesbutgoodies.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long id);

    void deleteByPostIdAndUserId(Long postId, Long id);
}
