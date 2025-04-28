package com.hm.oldiesbutgoodies.comment.domain;


import com.hm.oldiesbutgoodies.common.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.comment.dto.request.CommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    /** POST, SHOP 등 댓글이 달리는 도메인 구분 */
    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 20)
    private OwnerType ownerType;

    /** 실제 엔티티 PK (Post.id 또는 Shop.id) */
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    /* 대댓글을 위한 부모 댓글 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    /* 대댓글 목록 */
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    /* 부모 댓글 작성 */
    public static Comment from(CommentDto dto, OwnerType ownerType, Long ownerId) {
        return Comment.builder()
                .content(dto.getContent())
                .ownerType(ownerType)
                .ownerId(ownerId)
                .build();
    }

    /* 자식 댓글 작성 */
    public static Comment from(CommentDto dto, OwnerType ownerType, Long ownerId, Comment parentComment) {
        return Comment.builder()
                .content(dto.getContent())
                .ownerType(ownerType)
                .ownerId(ownerId)
                .parentComment(parentComment)
                .build();
    }

    public void update(CommentDto dto) {
        this.content = dto.getContent();
    }
}
