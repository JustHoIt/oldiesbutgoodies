package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.post.Post;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Builder
public class PostSummaryDto {
    private Long postId;
    private String title;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createAt;

    public static PostSummaryDto from(Post post) {
        return PostSummaryDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createAt(post.getCreatedAt())
                .build();
    }

}
