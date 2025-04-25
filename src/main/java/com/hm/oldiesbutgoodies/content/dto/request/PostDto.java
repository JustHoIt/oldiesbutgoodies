package com.hm.oldiesbutgoodies.content.dto.request;

import com.hm.oldiesbutgoodies.content.domain.ContentStatus;
import com.hm.oldiesbutgoodies.content.domain.Post;
import com.hm.oldiesbutgoodies.content.domain.Category;
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
    private ContentStatus postStatus;
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
