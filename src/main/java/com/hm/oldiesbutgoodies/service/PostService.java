package com.hm.oldiesbutgoodies.service;

import com.hm.oldiesbutgoodies.domain.ContentStatus;
import com.hm.oldiesbutgoodies.domain.post.Post;
import com.hm.oldiesbutgoodies.domain.user.User;
import com.hm.oldiesbutgoodies.dto.request.PostDto;
import com.hm.oldiesbutgoodies.dto.response.ResponseDto;
import com.hm.oldiesbutgoodies.exception.CustomException;
import com.hm.oldiesbutgoodies.exception.ErrorCode;
import com.hm.oldiesbutgoodies.repository.PostRepository;
import com.hm.oldiesbutgoodies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Transactional
    public ResponseDto createPost(String email, PostDto dto) {
        User user = findUserByEmail(email);
        Post post = Post.from(dto);
        post.setUser(user);

        postRepository.save(post);

        log.info("{} 님이 postId : {}, {} 라는 제목으로 글을 작성했습니다.", user.getName(), post.getId(), dto.getTitle());

        return ResponseDto.setMessage("글이 성공적으로 작성됐습니다.");
    }

    @Transactional
    public ResponseDto updatePost(String email, Long postId, PostDto dto) {
        User user = findUserByEmail(email);

        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException());

        validateOwner(post, user);

        post.update();

        log.info("{} 님이 postId : {}의 게시글을 수정했습니다.", user.getName(), post.getId());

        return ResponseDto.setMessage("글이 성공적으로 수정됐습니다.");
    }

    @Transactional
    public ResponseDto deletePost(String email, Long postId) {
        User user = findUserByEmail(email);

        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException());

        validateOwner(post, user);

        post.setDeleted(true);

        return ResponseDto.setMessage("Post ID : " + post.getId() + "글이 isDeleted= true 로 변경됐습니다. ");
    }

    public PostDto getPostById(String email, Long postId) {

        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = (email != null)
                ? findUserByEmail(email)
                : null;

        validateVisibility(post, user);

        PostDto postDto = new PostDto();

        return PostDto.from(post);
    }

    public Page<PostDto> listPosts(int page, int size,
                                   ContentVisibility visibility,
                                   ContentStatus status) {
        Pageable pageReq = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllByFilters(visibility, status, pageReq)
                .map(PostDto::from);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateOwner(Post post, User user) {
        if (!post.getUser().getId() == user.getId()) {
            throw new CustomException();
        }
    }
}
