package com.hm.oldiesbutgoodies.post.repository;

import com.hm.oldiesbutgoodies.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByIdAndDeletedFalse(Long id);

    Page<Post> findAllByUserIdAndDeletedFalse(Long id, Pageable pageable);

    /* 좋아요 수 증감 */
    @Modifying
    @Query("UPDATE Post p SET p.likeCount =  p.likeCount + :delta WHERE p.id = :postId and p.deleted = false")
    void updateLikeCount(@Param("postId") Long postId, @Param("delta") int delta);

    /* 북마크 수 증감 */
    @Modifying
    @Query("UPDATE Post  p SET p.bookmarkCount = p.bookmarkCount + :delta WHERE p.id = :postId and p.deleted = false")
    void updateBookmarkCount(@Param("postId") Long postId, @Param("delta") int delta);

    /* 조회수 증감*/
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId and p.deleted = false")
    void updateViewCount(@Param("postId") Long postId);

    /* 댓글 수 증감 */
    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + :delta WHERE p.id = :postId and p.deleted = false")
    void updateCommentCount(@Param("postId") Long postId, @Param("delta") int delta);
}
