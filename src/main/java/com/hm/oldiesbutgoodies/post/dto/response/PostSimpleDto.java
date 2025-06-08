package com.hm.oldiesbutgoodies.post.dto.response;

import com.hm.oldiesbutgoodies.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSimpleDto {
    private Long postId;
    private String title;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createAt;

    public static PostSimpleDto from(Post post) {
        return PostSimpleDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createAt(post.getCreatedAt())
                .build();
    }

}
