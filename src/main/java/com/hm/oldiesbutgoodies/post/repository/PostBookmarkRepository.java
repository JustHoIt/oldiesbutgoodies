package com.hm.oldiesbutgoodies.post.repository;

import com.hm.oldiesbutgoodies.post.domain.PostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long id);

    void deleteByPostIdAndUserId(Long postId, Long id);
}
