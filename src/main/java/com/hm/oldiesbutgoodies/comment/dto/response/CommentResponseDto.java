package com.hm.oldiesbutgoodies.comment.dto.response;

import com.hm.oldiesbutgoodies.comment.domain.Comment;
import com.hm.oldiesbutgoodies.user.domain.UserProfile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userNickname;
    private String userProfileImg;

    /* 최상위 댓글일 때 대댓글 리스트 */
    private List<CommentResponseDto> comments;

    public static CommentResponseDto from(Comment comment) {
        UserProfile p = comment.getUser().getUserProfile();

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .userId(comment.getUser().getId())
                .userNickname(p.getNickname())
                .userProfileImg(p.getProfileImg())
                .comments(new ArrayList<>())
                .build();
    }

}
