package com.hm.oldiesbutgoodies.post.dto.request;

import com.hm.oldiesbutgoodies.post.domain.PostStatus;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.post.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PostDto {

    private String title;
    private String content;
    private PostStatus postStatus;
    private Category category;
    private List<String> imageUrls;


    public static PostDto from(Post post, List<String> imageUrls) {
        return PostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .imageUrls(imageUrls)
                .build();
    }
}
