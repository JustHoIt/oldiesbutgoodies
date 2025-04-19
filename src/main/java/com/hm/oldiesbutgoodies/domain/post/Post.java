package com.hm.oldiesbutgoodies.domain.post;


import com.hm.oldiesbutgoodies.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.dto.request.PostDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus postStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private int commentCount = 0;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Post from(PostDto dto) {
        return Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postStatus(dto.getPostStatus())
                .category(dto.getCategory())
                .pinned(false)
                .build();
    }

    public void update(PostDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.postStatus = dto.getPostStatus();
        this.category = dto.getCategory();
    }

}
