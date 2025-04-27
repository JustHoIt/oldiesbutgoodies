package com.hm.oldiesbutgoodies.comment.service;

import com.hm.oldiesbutgoodies.comment.domain.Comment;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.product.domain.Product;
import com.hm.oldiesbutgoodies.product.repository.ProductRepository;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.comment.dto.request.CommentDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.comment.repository.CommentRepository;
import com.hm.oldiesbutgoodies.post.repository.PostRepository;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
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
    private final ProductRepository productRepository;

    @Transactional
    public ResponseDto createComment(String email, OwnerType ownerType, Long resourceId, CommentDto commentDto) {
        User user = findUserByEmail(email);

        findOwnerType(ownerType, resourceId);

        Comment comment = Comment.from(commentDto, ownerType, resourceId);
        comment.setUser(user);
        commentRepository.save(comment);

        String ownerT = ownerType.toString();
        log.info(ownerT);

        commentCount(ownerT, resourceId, +1);

        return ResponseDto.setMessage(user.getName() + "님이 댓글을 작성했습니다.");
    }

    @Transactional
    public ResponseDto updateComment(String email, OwnerType ownerType, Long resourceId, Long commentId, CommentDto commentDto) {
        findOwnerType(ownerType, resourceId);

        User user = findUserByEmail(email);
        Comment comment = loadExistingComment(commentId);

        validateOwner(comment, user);

        comment.update(commentDto);

        return ResponseDto.setMessage("");
    }


    /* 댓글 삭제(Soft-Delete) */
    @Transactional
    public ResponseDto deleteComment(String email, OwnerType ownerType, Long resourceId, Long commentId) {
        findOwnerType(ownerType, resourceId);

        User user = findUserByEmail(email);

        Comment comment = loadExistingComment(commentId);
        validateOwner(comment, user);
        log.info("댓글 작성 유저 : {},  로그인 유저 : {}", comment.getUser().getId(), user.getId());

        comment.setDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());

        String ownerT = ownerType.toString();
        log.info(ownerT);

        commentCount(ownerT, resourceId, -1);

        return ResponseDto.setMessage("Comment ID : " + commentId + "댓글이 삭제 처리 됐습니다. ");
    }

    public void commentCount(String s, long resourceId, int n) {
        if(s.equals("POST")){
            postRepository.updateCommentCount(resourceId, n);
        }else {
            productRepository.updateCommentCount(resourceId, n);
        }
    }


    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void findOwnerType(OwnerType ownerType, Long resourceId) {
        switch (ownerType) {
            case POST -> postRepository.findByIdAndDeletedFalse(resourceId)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
            case PRODUCT -> postRepository.findByIdAndDeletedFalse(resourceId)
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

    private Post findPost(long postId) {
        return postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Product findProduct(long productId) {
        return productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
