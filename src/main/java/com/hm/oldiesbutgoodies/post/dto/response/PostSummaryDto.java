package com.hm.oldiesbutgoodies.post.dto.response;

import com.hm.oldiesbutgoodies.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDto {
    private Long postId;
    private String title;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createAt;
    private String thumbnailUrl;

    public static PostSummaryDto from(Post post, String thumbnailUrl) {
        return PostSummaryDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createAt(post.getCreatedAt())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

}
