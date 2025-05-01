package com.hm.oldiesbutgoodies.comment.repository;

import com.hm.oldiesbutgoodies.comment.domain.Comment;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedFalse(Long commentId);

    Page<Comment> findAllByUserIdAndDeletedFalse(Long id, Pageable pageable);


    @Modifying
    @Query("""
                UPDATE Comment c
                   SET c.deleted = true
                 WHERE c.ownerType = :ownerType
                   AND c.ownerId   = :ownerId
            """)
    void softDeleteByOwner(@Param("ownerType") OwnerType ownerType, @Param("ownerId") Long ownerId);

    List<Comment> findAllByOwnerTypeAndOwnerIdAndDeletedFalse(OwnerType ownerType, Long postId);

    List<Comment> findAllByOwnerTypeAndOwnerIdAndParentCommentIsNullAndDeletedFalse(OwnerType ownerType, Long postId);

    List<Comment> findAllByParentCommentIdAndDeletedFalse(Long id);

}
