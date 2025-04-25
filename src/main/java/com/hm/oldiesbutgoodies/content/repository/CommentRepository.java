package com.hm.oldiesbutgoodies.content.repository;

import com.hm.oldiesbutgoodies.content.domain.Comment;
import com.hm.oldiesbutgoodies.content.domain.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedFalse(Long commentId);


    @Modifying
    @Query("""
                UPDATE Comment c
                   SET c.deleted = true
                 WHERE c.ownerType = :ownerType
                   AND c.ownerId   = :ownerId
            """)
    void softDeleteByOwner(@Param("ownerType") OwnerType ownerType, @Param("ownerId") Long ownerId);
}
