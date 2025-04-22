package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.ContentImage;
import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.post.Post;
import com.hm.oldiesbutgoodies.domain.post.Category;
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
