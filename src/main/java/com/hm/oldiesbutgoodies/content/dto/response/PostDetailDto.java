package com.hm.oldiesbutgoodies.content.dto.response;

import com.hm.oldiesbutgoodies.content.domain.Category;
import com.hm.oldiesbutgoodies.content.domain.Post;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.domain.UserProfile;
import lombok.*;

import java.util.List;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {
    private String title;
    private String content;
    private Category category;
    private List<String> imageUrls;
    private List<CommentResponseDto> comments;

    public static PostDetailDto from(Post post, List<String> imageUrls, List<CommentResponseDto> comments) {
        return PostDetailDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .imageUrls(imageUrls)
                .comments(comments)
                .build();
    }
}
