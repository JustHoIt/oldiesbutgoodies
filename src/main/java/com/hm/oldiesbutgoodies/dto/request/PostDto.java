package com.hm.oldiesbutgoodies.dto.request;

import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.post.PostType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDto {

    private String title;
    private String content;
    private ContentStatus postStatus;
    private PostType boardType;


}
