package com.hm.oldiesbutgoodies.post.dto.response;

import com.hm.oldiesbutgoodies.comment.dto.response.CommentResponseDto;
import com.hm.oldiesbutgoodies.post.domain.Category;
import com.hm.oldiesbutgoodies.post.domain.Post;
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
