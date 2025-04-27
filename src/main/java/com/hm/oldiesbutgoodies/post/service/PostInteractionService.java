package com.hm.oldiesbutgoodies.post.service;

import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.post.domain.PostBookmark;
import com.hm.oldiesbutgoodies.post.domain.PostLike;
import com.hm.oldiesbutgoodies.post.repository.PostBookmarkRepository;
import com.hm.oldiesbutgoodies.post.repository.PostLikeRepository;
import com.hm.oldiesbutgoodies.post.repository.PostRepository;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostInteractionService {

    private final PostRepository postRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto toggleLike(String email, Long postId) {
        User user = findUserByEmail(email);
        Post post = findPost(postId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postLikeRepository.deleteByPostIdAndUserId(postId, user.getId());
            post.decrementLikes();
            return ResponseDto.setMessage("좋아요를 취소 했습니다.");
        } else {
            postLikeRepository.save(PostLike.builder()
                    .user(user)
                    .post(post)
                    .build());
            post.incrementLikes();
            return ResponseDto.setMessage("좋아요를 눌렀습니다.");
        }
    }

    public ResponseDto toggleBookmark(String email, Long postId) {
        User user = findUserByEmail(email);
        Post post = findPost(postId);

        if (postBookmarkRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postBookmarkRepository.deleteByPostIdAndUserId(postId, user.getId());
            post.decrementBookmarks();
            return ResponseDto.setMessage("북마크를 취소 했습니다.");
        } else {
            postBookmarkRepository.save(PostBookmark.builder()
                    .user(user)
                    .post(post)
                    .build());
            post.incrementBookmarks();
            return ResponseDto.setMessage("게시글을 북마크를 했습니다.");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Post findPost(long postId) {
        return postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

}
