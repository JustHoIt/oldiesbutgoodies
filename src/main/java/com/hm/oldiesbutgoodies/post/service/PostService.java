package com.hm.oldiesbutgoodies.post.service;

import com.hm.oldiesbutgoodies.common.domain.ContentImage;
import com.hm.oldiesbutgoodies.post.domain.PostStatus;
import com.hm.oldiesbutgoodies.common.domain.OwnerType;
import com.hm.oldiesbutgoodies.post.domain.Category;
import com.hm.oldiesbutgoodies.post.domain.Post;
import com.hm.oldiesbutgoodies.common.service.FileStorageService;
import com.hm.oldiesbutgoodies.comment.dto.response.CommentResponseDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostDetailDto;
import com.hm.oldiesbutgoodies.comment.repository.CommentRepository;
import com.hm.oldiesbutgoodies.user.domain.User;
import com.hm.oldiesbutgoodies.post.dto.request.PostDto;
import com.hm.oldiesbutgoodies.post.dto.request.PostSummaryDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.common.exception.CustomException;
import com.hm.oldiesbutgoodies.common.exception.ErrorCode;
import com.hm.oldiesbutgoodies.common.repository.ContentImageRepository;
import com.hm.oldiesbutgoodies.post.repository.PostRepository;
import com.hm.oldiesbutgoodies.user.domain.UserProfile;
import com.hm.oldiesbutgoodies.user.repository.UserProfileRepository;
import com.hm.oldiesbutgoodies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final CommentRepository commentRepository;
    private final ContentImageRepository contentImageRepository;
    private final FileStorageService storage;


    //글 작성
    @Transactional
    public ResponseDto createPost(String email, PostDto dto, List<MultipartFile> files) {
        User user = findUserByEmail(email);
        Post post = Post.from(dto);
        post.setUser(user);

        postRepository.save(post);

        attachImages(post.getId(), files);

        //임시
        List<String> urls = contentImageRepository
                .findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType.POST, post.getId())
                .stream().map(ContentImage::getUrl).toList();

        log.info("{}", urls);
        log.info("{} 님이 postId : {}, {} 라는 제목으로 글을 작성했습니다.", user.getName(), post.getId(), dto.getTitle());

        return ResponseDto.setMessage("글이 성공적으로 작성됐습니다.");
    }

    //글 수정
    @Transactional
    public ResponseDto updatePost(String email, Long postId, PostDto dto, List<MultipartFile> files) {
        User user = findUserByEmail(email);

        Post post = loadExistingPost(postId);

        validateOwner(post, user);

        post.update(dto);

        attachImages(postId, files);
        //임시
        List<String> urls = contentImageRepository
                .findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType.POST, post.getId())
                .stream().map(ContentImage::getUrl).toList();

        log.info("{}", urls);

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

        commentRepository.softDeleteByOwner(OwnerType.POST, postId);

        return ResponseDto.setMessage("Post ID : " + post.getId() + "글이 삭제 처리 됐습니다. ");
    }

    //글 조회(단건 상세)
    public PostDetailDto getPostById(String email, Long postId) {
        User user = (email != null && !email.isBlank())
                ? findUserByEmail(email)
                : null;

        Post post = loadExistingPost(postId);
        checkVisibility(user, post);

        List<String> urls = contentImageRepository.findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(
                        OwnerType.POST, postId)
                .stream()
                .map(ContentImage::getUrl)
                .toList();

        List<CommentResponseDto> comments = commentRepository.findAllByOwnerTypeAndOwnerIdAndDeletedFalse(OwnerType.POST, postId)
                .stream()
                .map(comment -> {
                    UserProfile profile = user.getUserProfile();

                    return CommentResponseDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .userId(user.getId())
                            .userNickname(profile.getNickname())
                            .userProfileImg(profile.getProfileImg())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                })
                .toList();

        return PostDetailDto.from(post, urls, comments);
    }

    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.updateViewCount(postId);
    }

    //글 조회(리스트)
    public Page<PostSummaryDto> listPosts(String category,
                                          String keyword,
                                          Pageable pageable) {
        Category cg = null;
        if (!isBlank(category)) {
            cg = Category.valueOf(category.toUpperCase());
        }

        Page<Post> postPage;
        if (cg != null && isBlank(keyword)) {
            postPage = postRepository.findAllByCategoryAndDeletedFalse(cg, pageable);
        } else {
            postPage = postRepository.findAllByDeletedFalse(pageable);
        }

        return postPage.map(post -> {
            String thumb = contentImageRepository
                    .findAllByOwnerTypeAndOwnerIdOrderByPositionAsc(OwnerType.POST, post.getId())
                    .stream()
                    .findFirst()
                    .map(ContentImage::getUrl)
                    .orElse(null);

            return PostSummaryDto.from(post, thumb);
        });
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

    /* 이미지 업로드 공통 로직 */
    private void attachImages(Long postId, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) return;

        // 1) 기존 이미지 모두 삭제 (수정 시)
        contentImageRepository.deleteAllByOwnerTypeAndOwnerId(OwnerType.POST, postId);

        // 2) 새 이미지 저장
        int pos = 0;
        for (MultipartFile file : images) {
            String url = storage.store(file, OwnerType.valueOf(OwnerType.POST.name()));
            ContentImage ci = ContentImage.builder()
                    .ownerType(OwnerType.POST)
                    .ownerId(postId)
                    .url(url)
                    .position(pos++)
                    .build();
            contentImageRepository.save(ci);
        }
    }

    private void checkVisibility(User user, Post post) {
        PostStatus status = post.getPostStatus();

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
