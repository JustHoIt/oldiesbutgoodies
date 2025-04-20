package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.ContentImage;
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
    private String thumbnailUrl;

    public static PostSummaryDto from(Post post) {
        String thumb = post.getImages().stream()
                .findFirst()
                .map(ContentImage::getUrl)
                .orElse(null);

        return PostSummaryDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createAt(post.getCreatedAt())
                .thumbnailUrl(thumb)
                .build();
    }

}
