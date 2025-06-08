package com.hm.oldiesbutgoodies.user.service;

import com.hm.oldiesbutgoodies.comment.dto.response.CommentSimpleDto;
import com.hm.oldiesbutgoodies.comment.repository.CommentRepository;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.post.dto.response.PostSimpleDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostSummaryDto;
import com.hm.oldiesbutgoodies.post.repository.PostRepository;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public Page<PostSimpleDto> getMyPosts(String email, Pageable pageable) {
        User user = findUserByEmail(email);

        return postRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable)
                .map(PostSimpleDto::from);
    }

    public Page<CommentSimpleDto> getMyComments(String email, Pageable pageable) {
        User user = findUserByEmail(email);

        return commentRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable)
                .map(CommentSimpleDto::from);
    }

    public Page<PostSimpleDto> getMyPostLikes(String email, Pageable pageable) {
        User user = findUserByEmail(email);


        Page<Post> postPage = postRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable);
        // ✅TODO:
        return null;
    }

    public Page<PostSimpleDto> getMyPostBookmarks(String email, Pageable pageable) {
        User user = findUserByEmail(email);

        Page<Post> postPage = postRepository.findAllByUserIdAndDeletedFalse(user.getId(), pageable);
        // ✅TODO:
        return null;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

}
