package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.post.Post;
import com.hm.oldiesbutgoodies.domain.post.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PostDto {

    private String title;
    private String content;
    private ContentStatus postStatus;
    private Category category;


    public static PostDto from(Post post) {
        return PostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .build();
    }


}
