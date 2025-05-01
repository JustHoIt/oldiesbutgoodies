package com.hm.oldiesbutgoodies.post.controller;

import com.hm.oldiesbutgoodies.post.service.PostInteractionService;
import com.hm.oldiesbutgoodies.security.JwtProvider;
import com.hm.oldiesbutgoodies.post.dto.response.PostSummaryDto;
import com.hm.oldiesbutgoodies.post.dto.request.PostDto;
import com.hm.oldiesbutgoodies.common.dto.ResponseDto;
import com.hm.oldiesbutgoodies.post.dto.response.PostDetailDto;
import com.hm.oldiesbutgoodies.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostInteractionService postInteractionService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createPost(@RequestHeader("Authorization") String accessToken,
                                                  @RequestPart("post") @Valid PostDto dto,
                                                  @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.createPost(email, dto, images));
    }

    //수정
    @PutMapping(value = "/{postId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updatePost(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long postId,
                                                  @RequestPart("post") @Valid PostDto dto,
                                                  @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.updatePost(email, postId, dto, images));
    }


    // 삭제(soft-deleted)
    @DeleteMapping(value = "/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> deletePost(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long postId) {
        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postService.deletePost(email, postId));
    }

    // 조회
    @GetMapping(value = "/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDto> getPostById(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable Long postId) {
        String email = jwtProvider.getUserEmail(accessToken);
        PostDetailDto dto = postService.getPostById(email, postId);
        postService.incrementViewCount(postId);

        return ResponseEntity.ok(dto);
    }


    // 전체 조회
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostSummaryDto>> listPosts(@RequestParam(required = false) String category,
                                                          @RequestParam(required = false) String keyword,
                                                          Pageable pageable) {


        Page<PostSummaryDto> page = postService.listPosts(category, keyword, pageable);

        return ResponseEntity.ok(page);
    }

    @PostMapping(value = "/{postId}/like",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> toggleLike(@RequestHeader("Authorization") String accessToken,
                                     @PathVariable Long postId) {

        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postInteractionService.toggleLike(email, postId));

    }

    @PostMapping(value = "/{postId}/bookmark",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> toggleBookmark(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable Long postId) {

        String email = jwtProvider.getUserEmail(accessToken);

        return ResponseEntity.ok(postInteractionService.toggleBookmark(email, postId));

    }


}
