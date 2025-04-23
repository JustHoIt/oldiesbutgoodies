package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.Comment;
import com.hm.oldiesbutgoodies.domain.OwnerType;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.dto.request.CommentDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import com.hm.oldiesbutgoodies.repository.CommentRepository;
import com.hm.oldiesbutgoodies.repository.PostRepository;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto createComment(String email, OwnerType ownerType, Long resourceId, CommentDto commentDto) {
        User user = findUserByEmail(email);

        findOwnerType(ownerType, resourceId);

        Comment comment = Comment.from(commentDto, ownerType, resourceId);
        comment.setUser(user);
        commentRepository.save(comment);

        return ResponseDto.setMessage(user.getName() + "님이 댓글을 작성했습니다.");
    }


    @Transactional
    public ResponseDto updateComment(String email, OwnerType ownerType, Long resourceId , Long commentId, CommentDto commentDto) {
        findOwnerType(ownerType, resourceId);

        User user = findUserByEmail(email);
        Comment comment = loadExistingComment(commentId);

        validateOwner(comment, user);

        comment.update(commentDto);

        return ResponseDto.setMessage("");
    }


    /* 글 삭제(Soft-Delete) */
    @Transactional
    public ResponseDto deleteComment(String email, OwnerType ownerType, Long resourceId , Long commentId) {
        findOwnerType(ownerType, resourceId);

        User user = findUserByEmail(email);

        Comment comment = loadExistingComment(commentId);
        validateOwner(comment, user);
        log.info("댓글 작성 유저 : {},  로그인 유저 : {}", comment.getUser().getId(), user.getId());

        comment.setDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());


        return ResponseDto.setMessage("Comment ID : " + commentId + "댓글이 삭제 처리 됐습니다. ");
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void findOwnerType(OwnerType ownerType, Long resourceId) {
        switch (ownerType) {
            case POST -> postRepository.findByIdAndDeletedFalse(resourceId)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
            //🚨FIXME: SHOP 생기면 수정
            case SHOP -> postRepository.findByIdAndDeletedFalse(resourceId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        }
    }

    private Comment loadExistingComment(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateOwner(Comment comment, User user) {
        if (comment.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
        }
    }





}
