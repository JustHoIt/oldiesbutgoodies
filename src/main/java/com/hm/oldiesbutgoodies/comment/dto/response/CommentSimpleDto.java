package com.hm.oldiesbutgoodies.comment.dto.response;

import com.hm.oldiesbutgoodies.comment.domain.Comment;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Builder
public class CommentSimpleDto {
    private Long id;
    private String comment;
    private LocalDateTime createdAt;

    public static CommentSimpleDto from(Comment comment) {
        return CommentSimpleDto.builder()
                .id(comment.getId())
                .comment(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
