package com.hm.oldiesbutgoodies.product.repository;


import com.hm.oldiesbutgoodies.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedFalse(long productId);

    /* 좋아요 수 증감 */
    @Modifying
    @Query("UPDATE Product p SET p.likeCount =  p.likeCount + :delta WHERE p.id = :productId AND p.deleted = false")
    void updateLikeCount(@Param("productId") Long productId, @Param("delta") int delta);

    /* 북마크 수 증감 */
    @Modifying
    @Query("UPDATE Product  p SET p.bookmarkCount = p.bookmarkCount + :delta WHERE p.id = :productId AND p.deleted = false")
    void updateBookmarkCount(@Param("productId") Long productId, @Param("delta") int delta);

    /* 조회수 증감*/
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId AND p.deleted = false")
    void updateViewCount(@Param("productId") Long productId);

    /* 댓글 수 증감 */
    @Modifying
    @Query("UPDATE Product p SET p.commentCount = p.commentCount + :delta WHERE p.id = :productId and p.deleted = false")
    void updateCommentCount(@Param("productId") Long postId, @Param("delta") int delta);

}
