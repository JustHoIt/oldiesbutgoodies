package com.hm.oldiesbutgoodies.repository;

import com.hm.oldiesbutgoodies.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedFalse(Long commentId);


}
