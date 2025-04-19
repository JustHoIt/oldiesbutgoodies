package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.post.Category;
import com.hm.oldiesbutgoodies.domain.post.Post;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.dto.request.PostDto;
import com.hm.oldiesbutgoodies.dto.request.PostSummaryDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import com.hm.oldiesbutgoodies.repository.PostRepository;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;


    //글 작성
    @Transactional
    public ResponseDto createPost(String email, PostDto dto) {
        User user = findUserByEmail(email);
        Post post = Post.from(dto);
        post.setUser(user);

        postRepository.save(post);

        log.info("{} 님이 postId : {}, {} 라는 제목으로 글을 작성했습니다.", user.getName(), post.getId(), dto.getTitle());

        return ResponseDto.setMessage("글이 성공적으로 작성됐습니다.");
    }

    //글 수정
    @Transactional
    public ResponseDto updatePost(String email, Long postId, PostDto dto) {
        User user = findUserByEmail(email);

        Post post = loadExistingPost(postId);

        validateOwner(post, user);

        post.update(dto);

        log.info("{} 님이 postId : {}의 게시글을 수정했습니다.", user.getName(), post.getId());

        return ResponseDto.setMessage("글이 성공적으로 수정됐습니다.");
    }

    //글 삭제(soft-delete)
    @Transactional
    public ResponseDto deletePost(String email, Long postId) {
        User user = findUserByEmail(email);

        Post post = loadExistingPost(postId);

        validateOwner(post, user);
        log.info("게시글 작성 유저 : {},  로그인 유저 : {}", post.getUser().getId(), user.getId());

        post.setDeleted(true);
        post.setDeletedAt(LocalDateTime.now());

        return ResponseDto.setMessage("Post ID : " + post.getId() + "글이 isDeleted= true 로 변경됐습니다. ");
    }

    //글 조회(단건 상세)
    public PostDto getPostById(String email, Long postId) {
        User user = (email != null && !email.isBlank())
                ? findUserByEmail(email)
                : null;

        Post post = loadExistingPost(postId);

        checkVisibility(user, post);

        return PostDto.from(post);
    }

    //글 조회(리스트)
    public Page<PostSummaryDto> listPosts(String category,
                                          String keyword,
                                          Pageable pageable) {
        Category cg = Category.valueOf(category);
        if(!isBlank(category) && isBlank(keyword)) {
            return postRepository
                    .findAllByCategoryAndDeletedFalse(cg, pageable)
                    .map(PostSummaryDto::from);
        }

        return postRepository.findAllByDeletedFalse(pageable)
                .map(PostSummaryDto::from);
    }

    private boolean isBlank(String s) {
        return s == null || toString().isBlank();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateOwner(Post post, User user) {
        if (post.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
        }
    }

    private Post loadExistingPost(Long postId) {
        return postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void checkVisibility(User user, Post post) {
        ContentStatus status = post.getPostStatus();

        switch (status) {
            case PUBLIC -> {
                return;
            }
            case MEMBER_ONLY -> {
                if (user == null) {
                    throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
                }
                return;
            }
            case PRIVATE -> {
                if (user == null) {
                    throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
                }

                if (!post.getUser().getId().equals(user.getId())) {
                    throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
                }
                return;
            }
            case PROTECT -> {
                if (user == null) {
                    throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
                }
                if (!user.getRole().equals("ROLE_MANAGER")) {
                    throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
                }

                return;
            }

            default -> throw new CustomException(ErrorCode.USER_NOT_AUTHOR);
        }
    }
}
