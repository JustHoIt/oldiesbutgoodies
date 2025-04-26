package com.hm.oldiesbutgoodies.comment.dto.request;

import com.hm.oldiesbutgoodies.comment.domain.Comment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private String content;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .content(comment.getContent())
                .build();
    }
}
