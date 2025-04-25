package com.hm.oldiesbutgoodies.content.domain;


import com.hm.oldiesbutgoodies.common.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.content.dto.request.CommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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


    public static Comment from(CommentDto dto, OwnerType ownerType, Long ownerId) {
        return Comment.builder()
                .content(dto.getContent())
                .ownerType(ownerType)
                .ownerId(ownerId)
                .build();
    }

    public void update(CommentDto dto) {
        this.content = dto.getContent();
    }
}
